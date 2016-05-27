package pl.edu.pw.elka.sag.actors

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.messages.Messages.PrepareTweetInstance
import pl.edu.pw.elka.sag.tweets.{NEGATIVE, POSITIVE, Sentiment}
import weka.core.{Attribute, DenseInstance}

class PrepareTweetInstanceActor extends Actor{
  override def receive: Receive = {
    case PrepareTweetInstance(todelete, text, att) => {
      val columns = text.split(",").map(_.trim)

      if(columns.length == 5) {
        val sentiment = convertSentiment(columns(3))
        val tweetText = getTweetText(columns(4))

        if(tweetText != null) {
          println(s"Actor $this.name finished successful")
          sender ! Some(prepareInstance(sentiment, tweetText, att))
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

  def convertSentiment(oldLabel: String) : Sentiment = {
    oldLabel match {
      case ":(" => return NEGATIVE
      case ":)" => return POSITIVE
    }
  }

  def prepareInstance(sentiment: Sentiment, tweetText: String, attribute: Attribute): DenseInstance = {
    val instanceValue: Array[Double] = new Array[Double](2)
    instanceValue(0) = attribute.addStringValue(tweetText)
    instanceValue(1) = sentiment.value
    new DenseInstance(1.0, instanceValue)
  }
}
