package pl.edu.pw.elka.sag.utils

import pl.edu.pw.elka.sag.config.Configuration
import pl.edu.pw.elka.sag.model.TweetConversions.{TweetTextToString, toTweetText}
import pl.edu.pw.elka.sag.model.{NEGATIVE, POSITIVE, Sentiment}
import weka.core.{Attribute, DenseInstance}

class DenseInstanceBuilder {
  val wekaConfig = Configuration.getConfig()

  def buildDenseInstance(text: String, attribute: Attribute): Option[DenseInstance] = {
    val columns = getColumnsFromText(text)

    if (isNumberOfColumnsCorrect(columns))
      Some(buildDenseInstanceFromColumns(attribute, columns))
    else
      None
  }

  private def isNumberOfColumnsCorrect(columns: Array[String]): Boolean = {
    columns.length == 5
  }

  private def buildDenseInstanceFromColumns(att: Attribute, columns: Array[String]): DenseInstance = {
    val sentiment = convertSentiment(columns(3))
    val tweetText = getTweetText(columns(4))
    val denseInstance: DenseInstance = createDenseInstance(sentiment, tweetText, att)
    denseInstance
  }

  private def convertSentiment(oldLabel: String): Sentiment = {
    oldLabel match {
      case ":(" => NEGATIVE
      case ":)" => POSITIVE
      case _ => POSITIVE
    }
  }

  private def getTweetText(tweetText: String): String = {
    tweetText
      .removeUrl()
      .removePerson()
      .removeHashtag()
  }

  private def createDenseInstance(sentiment: Sentiment, tweetText: String, attribute: Attribute): DenseInstance = {
    val instanceValue: Array[Double] = new Array[Double](2)
    instanceValue(0) = attribute.addStringValue(tweetText)
    instanceValue(1) = sentiment.value
    new DenseInstance(1.0, instanceValue)
  }

  private def getColumnsFromText(text: String): Array[String] = {
    text.split(wekaConfig.delimiter).map(_.trim)
  }
}
