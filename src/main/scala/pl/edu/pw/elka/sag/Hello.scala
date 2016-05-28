package pl.edu.pw.elka.sag

import akka.actor.{ActorRef, ActorSystem}
import pl.edu.pw.elka.sag.actors.classify.ElectionsPredictionActor
import pl.edu.pw.elka.sag.actors.classify.Messages.StartTweetClassification
import pl.edu.pw.elka.sag.actors.createmodel.CreateModelActor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.BuildModel


object Hello {

  def main(args: Array[String]) {


    val system: ActorSystem = ActorSystem("Example")
    //    val actor: ActorRef = ActorSystem("Example").actorOf(Props[CreateModelActor], name = "test")
    val actor: ActorRef = system.actorOf(CreateModelActor.props(model => {
      println(model)
      val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model))
      electionsPrediction ! StartTweetClassification
    }))
    actor ! BuildModel
    //    actor ? BuildModel onComplete {
//      case Success(model: Model) => {
//        println(model)
//        val electionsPrediction = system.actorOf(ElectionsPredictionActor.props(model))
//        electionsPrediction ! StartTweetClassification
//      }
//      case Failure(t) => println("nie udalo sie wyznaczyc modelu")
//    }
  }

}
