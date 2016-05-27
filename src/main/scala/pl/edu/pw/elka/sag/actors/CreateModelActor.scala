package pl.edu.pw.elka.sag.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import pl.edu.pw.elka.sag.actors.messages.Messages.{PrepareTweetInstance, StartMessage}
import pl.edu.pw.elka.sag.tweets.POSITIVE
import pl.edu.pw.elka.sag.weka.Weka
import weka.core.Instance

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

class CreateModelActor extends Actor{
  implicit val timeout = Timeout(5 seconds)

  override def receive: Receive = {
    case StartMessage => {
      val weka = new Weka()
      val file = "C:\\Users\\Miko\\training.txt"
      val bufferedSource = io.Source.fromFile(file)


      val instances = weka.prepareInstances()
      for (line <- bufferedSource.getLines) {
        val childActor = context.actorOf(Props[PrepareTweetInstanceActor])
        val instance: PrepareTweetInstance = PrepareTweetInstance(POSITIVE, line, instances.attribute(0))
       val future: Future[Option[Instance]] = (childActor ? instance).mapTo[Option[Instance]]
        val result: Option[Instance] = Await.result(future, 10 seconds)
        result.map(i => instances.add(i))
      }
      bufferedSource.close
      weka.filter(instances)
    }
  }


}
