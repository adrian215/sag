package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.{CannotCreateTweetInstance, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.classification.setup.DenseInstanceBuilder

class PrepareTweetInstanceActor extends Actor {

  val denseInstanceBuilder = new DenseInstanceBuilder

  override def receive: Receive = {
    case PrepareTweetInstance(text, att) => {

      val instance = denseInstanceBuilder.buildDenseInstance(text, att)
      instance match {
        case Some(instance) =>
          println(s"Actor $this.name finished successful")
          sender ! TweetInstanceCreated(instance)
        case None =>
          println(s"Actor $this.name failed")
          sender ! CannotCreateTweetInstance
      }
    }
  }
}
