package pl.edu.pw.elka.sag.actors

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.messages.Messages.StartMessage

class CreateModelActor extends Actor{
  override def receive: Receive = {
    case StartMessage => {

    }
  }
}
