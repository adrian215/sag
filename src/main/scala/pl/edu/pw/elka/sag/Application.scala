package pl.edu.pw.elka.sag

import akka.actor.{ActorSystem, ActorRef}
import pl.edu.pw.elka.sag.actors.classify.ElectionsPredictionActor
import pl.edu.pw.elka.sag.actors.classify.Messages.StartTweetClassification
import pl.edu.pw.elka.sag.actors.createmodel.CreateModelActor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.BuildModel
import pl.edu.pw.elka.sag.config.{Configuration, WekaConfig}
import pl.edu.pw.elka.sag.model.{CandidatePopularity, AlgorithmModel}
import pl.edu.pw.elka.sag.weka.{WekaFileHandler, ClsType}

import scala.io.StdIn

class Application {

  val system: ActorSystem = ActorSystem("Example")

  def start(): Unit = {
    println("Config file path?")
    val configPath = StdIn.readLine()
    val config = Configuration.setConfigPath(configPath)
    val wekaConfig: WekaConfig = Configuration.getConfig()

    println("Algorithm type [svm, svmnl, nb]:")
    val classifierTypeIn = StdIn.readLine().toUpperCase()
    val classifierType = ClsType.valueOf(classifierTypeIn)

    println("Build model? [y/n]")
    val buildModelIn = StdIn.readLine()
    val buildModel = if (buildModelIn == "n") false else true

    println("Algorithm: " + classifierTypeIn + "\nBuild model: " + buildModel)

    if (buildModel) {
      classifyWithNewModel(classifierType)
    } else {
      classifyWithExistingModel(classifierType)
    }
  }

  private def classifyWithNewModel(classifierType: ClsType): Unit = {
    val wekaFileHandler = new WekaFileHandler()
    val actor: ActorRef = system.actorOf(CreateModelActor.props(classifierType, model => {
      wekaFileHandler.saveClassifier(model.classifier, classifierType)
      wekaFileHandler.saveFilter(model.filter, classifierType)
      val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model, finished))
      electionsPrediction ! StartTweetClassification
    }))
    actor ! BuildModel
  }

  private def classifyWithExistingModel(classifierType: ClsType): Unit = {
    val wekaFileHandler = new WekaFileHandler()
    val classifier = wekaFileHandler.loadClassifier(classifierType)
    val filter = wekaFileHandler.loadFilter(classifierType)
    val model = AlgorithmModel(classifier, filter)
    val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model, finished))
    electionsPrediction ! StartTweetClassification
  }

  private def finished(popularity: List[CandidatePopularity]): Unit = {
    system.terminate()
    println(s"\nWyniki klasyfikacji: $popularity")
  }
}
