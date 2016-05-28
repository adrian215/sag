package pl.edu.pw.elka.sag

import _root_.weka.classifiers.Classifier
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import pl.edu.pw.elka.sag.actors.createmodel.CreateModelActor
import pl.edu.pw.elka.sag.actors.messages.Messages.BuildModel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import pl.edu.pw.elka.sag.config.ApplicationConfig.timeout


object Hello {

  def main(args: Array[String]) {


    //    val actor: ActorRef = ActorSystem("Example").actorOf(Props[CreateModelActor], name = "test")
    val actor: ActorRef = ActorSystem("Example").actorOf(Props[CreateModelActor])
    actor ? BuildModel onComplete {
      case Success(model: Classifier) => println(model)
      case Failure(t) => println("nie udalo sie wyznaczyc modelu")
    }
  }

}
