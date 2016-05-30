package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.{CannotCreateTweetInstance, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.utils.DenseInstanceBuilder

class PrepareTweetInstanceActor extends Actor {

  val denseInstanceBuilder = new DenseInstanceBuilder

  override def receive: Receive = {
    case PrepareTweetInstance(text, att) =>
      val instance = denseInstanceBuilder.buildDenseInstance(text, att)
      val message = instance map (TweetInstanceCreated(_)) getOrElse CannotCreateTweetInstance
      sender ! message
  }
}
