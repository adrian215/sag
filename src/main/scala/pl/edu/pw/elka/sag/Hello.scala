package pl.edu.pw.elka.sag

import akka.actor.{ActorRef, ActorSystem}
import pl.edu.pw.elka.sag.actors.classify.ElectionsPredictionActor
import pl.edu.pw.elka.sag.actors.classify.Messages.StartTweetClassification
import pl.edu.pw.elka.sag.actors.createmodel.CreateModelActor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.BuildModel
import pl.edu.pw.elka.sag.weka.ClsType


object Hello {

  def main(args: Array[String]) {

    val system: ActorSystem = ActorSystem("Example")
    val actor: ActorRef = system.actorOf(CreateModelActor.props(ClsType.SVM, model => {
      val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model))
      electionsPrediction ! StartTweetClassification
    }))
    actor ! BuildModel
  }

}
