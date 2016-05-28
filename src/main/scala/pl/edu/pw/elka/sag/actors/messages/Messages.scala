package pl.edu.pw.elka.sag.actors.messages

import pl.edu.pw.elka.sag.sentiments.Sentiment
import weka.core.{DenseInstance, Attribute}

object Messages {
  case class BuildModel()

  case class PrepareTweetInstance(sentiment: Sentiment, text: String, att: Attribute)
  case class TweetInstanceCreated(instance: DenseInstance)
  case class CannotCreateTweetInstance()

  case class StartTweetClassification()
}
