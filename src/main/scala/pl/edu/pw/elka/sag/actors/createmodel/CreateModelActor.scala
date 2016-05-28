package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.Props
import akka.routing.RoundRobinPool
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.createmodel.CreateModelActor.ModelCreated
import pl.edu.pw.elka.sag.actors.createmodel.Messages.{BuildModel, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.classification.Model
import pl.edu.pw.elka.sag.config.{ApplicationConfig, WekaConfig}
import pl.edu.pw.elka.sag.sentiments.POSITIVE
import pl.edu.pw.elka.sag.weka.{ClsType, Weka}
import weka.classifiers.Classifier

object CreateModelActor {
  type ModelCreated = (Model) => Unit
  def props(modelCreated: ModelCreated): Props = Props(new CreateModelActor(modelCreated))
}

class CreateModelActor(modelCreated: ModelCreated) extends MasterActor {
  override val workers = context.actorOf(RoundRobinPool(ApplicationConfig.actorSize).props(Props[PrepareTweetInstanceActor]))

  val weka = new Weka()
  val instances = weka.prepareInstances()

  override def receive: Receive = {
    case BuildModel => {
      println("Starting main actor")

      val tweets = io.Source.fromFile(WekaConfig.trainingFile)
      tweets.getLines().foreach(tweet => {
        val instance = PrepareTweetInstance(POSITIVE, tweet, instances.attribute(0))
        spawnChildWithMessage(instance)
      })

      tweets close
    }

    case TweetInstanceCreated(instance) => {
      //      println(s"Received TweetInstance from ${sender}")
      childActorFinished()
      instances.add(instance)
    }

    case _ => {
      println(s"${sender} finished with errors")
      childActorFinished()
    }
  }


  override def finishCurrentActor(): Unit = {
    println("Main actor finished")
    val filter = weka.createFilter(instances)
    val filteredData = weka.filter(instances, filter)
    val model: Classifier = weka.buildModel(filteredData, ClsType.SVM)
    modelCreated.apply(Model(model, filter))
  }

}
