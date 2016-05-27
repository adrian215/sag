package pl.edu.pw.elka.sag

import _root_.weka.classifiers.Classifier
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.routing.RoundRobinPool
import akka.util.Timeout
import pl.edu.pw.elka.sag.actors.CreateModelActor
import pl.edu.pw.elka.sag.actors.messages.Messages.BuildModel

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Hello {
  implicit val timeout = Timeout(100 seconds)

  def main(args: Array[String]) {

  }

  //    val actor: ActorRef = ActorSystem("Example").actorOf(Props[CreateModelActor], name = "test")
  val actor: ActorRef = ActorSystem("Example").actorOf(RoundRobinPool(5).props(Props[CreateModelActor]))
  actor ? BuildModel onComplete {
    case Success(model: Classifier) => {
      println(model)
      println(model)
    }
    case Failure(t) => println("nie udalo sie wyznaczyc modelu")
  }
  print("koniec")
}
