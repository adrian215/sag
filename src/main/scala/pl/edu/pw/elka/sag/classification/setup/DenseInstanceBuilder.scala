package pl.edu.pw.elka.sag.classification.setup

import pl.edu.pw.elka.sag.sentiments.{Sentiment, POSITIVE, NEGATIVE}
import weka.core.{DenseInstance, Attribute}

import pl.edu.pw.elka.sag.utils.TweetConversion.toTweetText

class DenseInstanceBuilder {

  def buildDenseInstanceFromColumns(att: Attribute, columns: Array[String]): DenseInstance = {
    val sentiment = convertSentiment(columns(3))
    val tweetText = getTweetText(columns(4))
    val denseInstance: DenseInstance = createDenseInstance(sentiment, tweetText, att)
    denseInstance
  }

  def convertSentiment(oldLabel: String): Sentiment = {
    oldLabel match {
      case ":(" => NEGATIVE
      case ":)" => POSITIVE
      case _ => POSITIVE
    }
  }

  def getTweetText(rawText: String): String = {
    rawText
      .removeUrl()
      .removePerson()
      .removeHashtag()
      .toString()
  }

  def createDenseInstance(sentiment: Sentiment, tweetText: String, attribute: Attribute): DenseInstance = {
    val instanceValue: Array[Double] = new Array[Double](2)
    instanceValue(0) = attribute.addStringValue(tweetText)
    instanceValue(1) = sentiment.value
    new DenseInstance(1.0, instanceValue)
  }
}
