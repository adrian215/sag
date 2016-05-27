package pl.edu.pw.elka.sag

import akka.actor.{ActorRef, ActorSystem, Props}
import pl.edu.pw.elka.sag.actors.CreateModelActor
import pl.edu.pw.elka.sag.actors.messages.Messages.StartMessage



object Hello {

  def main(args: Array[String]) {




  }

    val actor: ActorRef = ActorSystem("Example").actorOf(Props[CreateModelActor], name = "test")
    actor ! StartMessage
}
