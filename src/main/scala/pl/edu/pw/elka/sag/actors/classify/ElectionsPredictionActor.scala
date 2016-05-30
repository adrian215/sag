package pl.edu.pw.elka.sag.actors.classify

import akka.actor.{Props, ActorRef}
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.classify.Messages.{PredictCandidate, StartTweetClassification}
import pl.edu.pw.elka.sag.config.WekaConfig
import pl.edu.pw.elka.sag.model.AlgorithmModel

object ElectionsPredictionActor {
  def props(model: AlgorithmModel): Props = Props(new ElectionsPredictionActor(model))
}

class ElectionsPredictionActor(val model: AlgorithmModel) extends MasterActor{
  override def workers: ActorRef = context.actorOf(Props[SingleCandidatePopularityPredictingActor])

  override def receive: Receive = {
    case StartTweetClassification =>
      WekaConfig.classificationFiles.foreach(file => {
        val message: PredictCandidate = PredictCandidate(model, file)
        spawnChildWithMessage(message)
      })
  }

  override def finishCurrentActor(): Unit = ???
}
