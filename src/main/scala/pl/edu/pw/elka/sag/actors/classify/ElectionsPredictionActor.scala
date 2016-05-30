package pl.edu.pw.elka.sag.actors.classify

import akka.actor.{Props, ActorRef}
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.classify.Messages.{CandidatePredicted, PredictCandidate, StartTweetClassification}
import pl.edu.pw.elka.sag.config.WekaConfig
import pl.edu.pw.elka.sag.model.{CandidatePopularity, AlgorithmModel}

import scala.collection.mutable.ArrayBuffer

object ElectionsPredictionActor {
  def props(model: AlgorithmModel, classificationFinished: ClassificationFinished): Props =
    Props(new ElectionsPredictionActor(model, classificationFinished))
}

class ElectionsPredictionActor(val model: AlgorithmModel,
                               classificationFinished: ClassificationFinished) extends MasterActor{

  override def workers: ActorRef = context.actorOf(Props[SingleCandidatePopularityPredictingActor])
  val classifiedCandidatesPopularity = ArrayBuffer.empty[CandidatePopularity]

  override def receive: Receive = {
    case StartTweetClassification => delegateClassificationToChildren()
    case CandidatePredicted(popularity) => savePrediction(popularity)
    case _ => childActorFailed()
  }

  def savePrediction(popularity: CandidatePopularity): Unit = {
    classifiedCandidatesPopularity += popularity
    childActorFinished()
  }

  def delegateClassificationToChildren(): Unit = {
    WekaConfig.classificationFiles.foreach { case(candidate, fileName) =>
      val message: PredictCandidate = PredictCandidate(candidate, model, fileName)
      spawnChildWithMessage(message)
    }
  }

  override def finishCurrentActor(): Unit = {
    classificationFinished.apply(classifiedCandidatesPopularity.toList)
  }
}
