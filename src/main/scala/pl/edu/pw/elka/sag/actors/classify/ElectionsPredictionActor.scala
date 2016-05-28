package pl.edu.pw.elka.sag.actors.classify

import akka.actor.{Props, ActorRef}
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.classify.Messages.{PredictCandidate, StartTweetClassification}
import pl.edu.pw.elka.sag.classification.Model
import pl.edu.pw.elka.sag.config.WekaConfig

object ElectionsPredictionActor {
  def props(model: Model): Props = Props(new ElectionsPredictionActor(model))
}

class ElectionsPredictionActor(val model: Model) extends MasterActor{
  override def workers: ActorRef = context.actorOf(Props[SingleCandidatePopularityPredictingActor])

  override def receive: Receive = {
    case StartTweetClassification =>
      WekaConfig.classificationFiles.foreach(file => workers ! PredictCandidate(model, file))
  }

  override def finishCurrentActor(): Unit = ???
}
