package pl.edu.pw.elka.sag.actors

import java.util

import akka.actor.Actor
import pl.edu.pw.elka.sag.WekaTest
import pl.edu.pw.elka.sag.actors.messages.Messages.StartMessage

class CreateModelActor extends Actor{
  override def receive: Receive = {
    case StartMessage => {
      val file = "C:\\Users\\Miko\\training.txt"
      val bufferedSource = io.Source.fromFile(file)

      for (line <- bufferedSource.getLines) {

      }
      bufferedSource.close

    }
  }


}
