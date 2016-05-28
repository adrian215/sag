package pl.edu.pw.elka.sag.actors.createmodel

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.messages.Messages.{CannotCreateTweetInstance, PrepareTweetInstance, TweetInstanceCreated}
import pl.edu.pw.elka.sag.classification.setup.DenseInstanceBuilder
import pl.edu.pw.elka.sag.config.WekaConfig
import weka.core.DenseInstance

class PrepareTweetInstanceActor extends Actor {

  val denseInstanceBuilder = new DenseInstanceBuilder

  override def receive: Receive = {
    case PrepareTweetInstance(todelete, text, att) => {
      val columns = getColumnsFromText(text)

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

  def getColumnsFromText(text: String): Array[String] = {
    text.split(WekaConfig.delimiter).map(_.trim)
  }

  def isNumberOfColumnsCorrect(columns: Array[String]): Boolean = {
    columns.length == 5
  }
}
