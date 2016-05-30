package pl.edu.pw.elka.sag

import akka.actor.{ActorRef, ActorSystem}
import pl.edu.pw.elka.sag.actors.classify.ElectionsPredictionActor
import pl.edu.pw.elka.sag.actors.classify.Messages.StartTweetClassification
import pl.edu.pw.elka.sag.actors.createmodel.CreateModelActor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.BuildModel
import pl.edu.pw.elka.sag.model.AlgorithmModel
import pl.edu.pw.elka.sag.weka.{ClsType, WekaFileHandler}

import scala.io
import scala.io.StdIn


object Hello {

  def main(args: Array[String]) {
    println("Algorithm type [svm, nb]:")
    val classifierTypeIn = StdIn.readLine().toUpperCase()
    val classifierType = ClsType.valueOf(classifierTypeIn)

    println("Build model? [y/n]")
    val buildModelIn = StdIn.readLine()
    val buildModel = if(buildModelIn == "n") false else true

    println("Algorithm: " + classifierTypeIn + "\nBuild model: " + buildModel )

    val wekaFileHandler = new WekaFileHandler()
    val system: ActorSystem = ActorSystem("Example")

    if (buildModel) {
      val actor: ActorRef = system.actorOf(CreateModelActor.props(classifierType, model => {
        wekaFileHandler.saveClassifier(model.classifier, classifierType)
        wekaFileHandler.saveFilter(model.filter, classifierType)
        val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model))
        electionsPrediction ! StartTweetClassification
      }))
      actor ! BuildModel
    } else {
      val classifier = wekaFileHandler.loadClassifier(classifierType)
      val filter = wekaFileHandler.loadFilter(classifierType)
      val model = AlgorithmModel(classifier, filter)
      val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model))
      electionsPrediction ! StartTweetClassification
    }
  }

}
