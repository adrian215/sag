package pl.edu.pw.elka.sag.actors

import akka.actor.{Actor, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import pl.edu.pw.elka.sag.actors.messages.Messages.{BuildModel, PrepareTweetInstance}
import pl.edu.pw.elka.sag.config.WekaConfig
import pl.edu.pw.elka.sag.tweets.POSITIVE
import pl.edu.pw.elka.sag.weka.{ClsType, Weka}
import weka.core.{Instance, Instances}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.BufferedSource

class CreateModelActor extends Actor {
  implicit val timeout = Timeout(100 seconds)
  val actorsSize = 1000

  val config = new WekaConfig
  val weka = new Weka()
  val tweetWorkers = context.actorOf(RoundRobinPool(actorsSize).props(Props[PrepareTweetInstanceActor]))

  override def receive: Receive = {
    case BuildModel => {
      val tweets = io.Source.fromFile(config.file)

      val instances: Instances = getTweetInstances(weka, tweets)

      tweets.close
      val filteredData= weka.filter(instances)
      sender ! weka.buildModel(filteredData, ClsType.SVM)
    }
  }


  def getTweetInstances(weka: Weka, tweets: BufferedSource): Instances = {
    val instances = weka.prepareInstances()

    val calledTweets = for (tweet <- tweets.getLines) yield {
      val instance: PrepareTweetInstance = PrepareTweetInstance(POSITIVE, tweet, instances.attribute(0))
      (tweetWorkers ? instance).mapTo[Option[Instance]]
    }
    val tweetsFutures = Future.sequence(calledTweets.toList)

    Await.result(tweetsFutures, 100 seconds)
      .filter(i => i.isDefined)
      .map(i => instances.add(i.get))
    instances
  }
}
