package pl.edu.pw.elka.sag.classification.setup

import pl.edu.pw.elka.sag.config.WekaConfig
import pl.edu.pw.elka.sag.utils.TweetConversion.toTweetText
import pl.edu.pw.elka.sag.{NEGATIVE, POSITIVE, Sentiment}
import weka.core.{Attribute, DenseInstance}

class DenseInstanceBuilder {

  def buildDenseInstance(text: String, attribute: Attribute): Option[DenseInstance] = {
    val columns = getColumnsFromText(text)

    if (isNumberOfColumnsCorrect(columns))
      Some(buildDenseInstanceFromColumns(attribute, columns))
    else
      None
  }

  private[DenseInstanceBuilder] def isNumberOfColumnsCorrect(columns: Array[String]): Boolean = {
    columns.length == 5
  }

  private[DenseInstanceBuilder] def buildDenseInstanceFromColumns(att: Attribute, columns: Array[String]): DenseInstance = {
    val sentiment = convertSentiment(columns(3))
    val tweetText = getTweetText(columns(4))
    val denseInstance: DenseInstance = createDenseInstance(sentiment, tweetText, att)
    denseInstance
  }

  private[DenseInstanceBuilder] def convertSentiment(oldLabel: String): Sentiment = {
    oldLabel match {
      case ":(" => NEGATIVE
      case ":)" => POSITIVE
      case _ => POSITIVE
    }
  }

  private[DenseInstanceBuilder] def getTweetText(rawText: String): String = {
    rawText
      .removeUrl()
      .removePerson()
      .removeHashtag()
      .toString()
  }

  private[DenseInstanceBuilder] def createDenseInstance(sentiment: Sentiment, tweetText: String, attribute: Attribute): DenseInstance = {
    val instanceValue: Array[Double] = new Array[Double](2)
    instanceValue(0) = attribute.addStringValue(tweetText)
    instanceValue(1) = sentiment.value
    new DenseInstance(1.0, instanceValue)
  }

  private[DenseInstanceBuilder] def getColumnsFromText(text: String): Array[String] = {
    text.split(WekaConfig.delimiter).map(_.trim)
  }
}
