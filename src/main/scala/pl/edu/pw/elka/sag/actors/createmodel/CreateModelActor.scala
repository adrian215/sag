package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinPool
import pl.edu.pw.elka.sag.actors.messages.Messages.{BuildModel, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.config.{ApplicationConfig, WekaConfig}
import pl.edu.pw.elka.sag.sentiments.POSITIVE
import pl.edu.pw.elka.sag.weka.{ClsType, Weka}
import weka.classifiers.Classifier

class CreateModelActor extends Actor {
  val weka = new Weka()
  val tweetWorkers = context.actorOf(RoundRobinPool(ApplicationConfig.actorSize).props(Props[PrepareTweetInstanceActor]))
  //  val tweetWorkers = context.actorOf(Props[PrepareTweetInstanceActor])
  val instances = weka.prepareInstances()
  var allToWork: Int = 0
  var currentWorked = 0

  override def receive: Receive = {
    case BuildModel => {
      println("Starting main actor")

      val tweets = io.Source.fromFile(WekaConfig.file)
      tweets.getLines().foreach(tweet => {
        val instance = PrepareTweetInstance(POSITIVE, tweet, instances.attribute(0))
        childActorSpawned()
//        println(s"Sending message ${instance} to child actor")
        tweetWorkers ! instance
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


  def childActorSpawned(): Unit = {
    allToWork += 1
  }

  def childActorFinished(): Unit = {
    currentWorked += 1
    println(s"Finished $currentWorked / $allToWork")
    if (allChildrenFinished) {
      println("Main actor finished")
      val filteredData = weka.filter(instances)
      val model: Classifier = weka.buildModel(filteredData, ClsType.SVM)
      sender ! model
    }
  }

  def allChildrenFinished: Boolean = {
    currentWorked == allToWork
  }
}
