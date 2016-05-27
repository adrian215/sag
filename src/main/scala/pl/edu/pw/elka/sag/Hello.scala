package pl.edu.pw.elka.sag

import akka.actor.{ActorRef, Props, ActorSystem}

object Hello {

  def main(args: Array[String]) {
    print("Hello world!")
    val actor: ActorRef = ActorSystem("Example").actorOf(Props[ExampleActor], name = "test")
    actor ! "hello"
    actor ? "pytanie"
  }
}
