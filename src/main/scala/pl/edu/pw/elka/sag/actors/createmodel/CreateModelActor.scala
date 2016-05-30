package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.Props
import akka.routing.RoundRobinPool
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.{BuildModel, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.config.{ApplicationConfig, WekaConfig}
import pl.edu.pw.elka.sag.model.AlgorithmModel
import pl.edu.pw.elka.sag.weka.{ClsType, Weka}
import weka.classifiers.Classifier
import weka.core.DenseInstance

import scala.io.BufferedSource

object CreateModelActor {
  def props(modelCreated: ModelCreated): Props = Props(new CreateModelActor(modelCreated))
}

class CreateModelActor(modelCreated: ModelCreated) extends MasterActor {
  override val workers = context.actorOf(RoundRobinPool(ApplicationConfig.actorSize).props(Props[PrepareTweetInstanceActor]))

  val weka = new Weka()
  val instances = weka.prepareInstances()

  override def receive: Receive = {
    case BuildModel => buildModel()
    case TweetInstanceCreated(instance) => receiveTweetInstance(instance)
    case _ => childActorFailed()
  }


  private def buildModel(): Unit = {
    println("Starting main actor")

    val tweets = io.Source.fromFile(WekaConfig.trainingFile)
    delegateTweetInstanceCreationToChildren(tweets)
    tweets close
  }

  private def delegateTweetInstanceCreationToChildren(tweets: BufferedSource): Unit = {
    tweets.getLines().foreach(tweet => {
      val instance = PrepareTweetInstance(tweet, instances.attribute(0))
      spawnChildWithMessage(instance)
    })
  }

  private def receiveTweetInstance(instance: DenseInstance): Unit = {
    childActorFinished()
    instances add instance
  }

  override protected def finishCurrentActor(): Unit = {
    println("Main actor finished")
    val filter = weka.createFilter(instances)
    val filteredData = weka.filter(instances, filter)
    val model: Classifier = weka.buildModel(filteredData, ClsType.SVM)
    modelCreated.apply(AlgorithmModel(model, filter))
  }
}
