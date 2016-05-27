package pl.edu.pw.elka.sag

import akka.actor.Actor

class ExampleActor extends Actor{
  override def receive: Receive = {
    case _ => println("test")
  }
}
