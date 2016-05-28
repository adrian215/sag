package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.createmodel.Messages.{CannotCreateTweetInstance, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.classification.setup.DenseInstanceBuilder
import weka.core.DenseInstance

class PrepareTweetInstanceActor extends Actor {

  val denseInstanceBuilder = new DenseInstanceBuilder

  override def receive: Receive = {
    case PrepareTweetInstance(todelete, text, att) => {
      val columns = denseInstanceBuilder.getColumnsFromText(text)

      if (isNumberOfColumnsCorrect(columns)) {
        val denseInstance: DenseInstance = denseInstanceBuilder.buildDenseInstanceFromColumns(att, columns)

        println(s"Actor $this.name finished successful")
        sender ! TweetInstanceCreated(denseInstance)
      } else {
        println(s"Actor $this.name failed")
        sender ! CannotCreateTweetInstance
      }
    }
  }

  def isNumberOfColumnsCorrect(columns: Array[String]): Boolean = {
    columns.length == 5
  }
}
