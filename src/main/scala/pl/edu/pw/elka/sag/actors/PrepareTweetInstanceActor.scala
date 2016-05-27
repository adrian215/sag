package pl.edu.pw.elka.sag.actors

import akka.actor.{Actor, DeadLetter}
import akka.actor.Actor.Receive
import com.sun.xml.internal.stream.Entity.ScannedEntity
import pl.edu.pw.elka.sag.actors.messages.Messages.PrepareTweetInstance
import pl.edu.pw.elka.sag.tweets.Sentiment
import weka.core.{Attribute, DenseInstance}

class PrepareTweetInstanceActor extends Actor{
  override def receive: Receive = {
    case PrepareTweetInstance(sentiment, text, att) => {
      val cols = text.split(",").map(_.trim)

      if(cols.length == 5) {
        val label = convertSentiment()
        val tweetText = getTweetText(cols(4))

        if(tweetText != null) {
          sender ! prepareInstance(sentiment, tweetText, att)
        }
      }



      sender ! None
    }
  }

  def getTweetText(rawText: String) : String = {
    if(rawText.matches(".*\\bRT\\b.*")) {
      return null
    }

    //remove url
    var text = rawText.replaceAll("https?://\\S+\\s?", "")

    //remove person
    text = text.replaceAll("@\\S*", "")

    //remove hashtag
    text = text.replaceAll("#\\S*", "")

    text
  }

  def convertSentiment() : Int = {
    0
  }

  def prepareInstance(sentiment: Sentiment, tweetText: String, attribute: Attribute): DenseInstance = {
    val instanceValue: Array[Double] = new Array[Double](2)
    instanceValue(0) = attribute.addStringValue(tweetText)
    instanceValue(1) = 0
    new DenseInstance(1.0, instanceValue)
  }
}
