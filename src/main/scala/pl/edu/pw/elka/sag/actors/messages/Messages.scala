package pl.edu.pw.elka.sag.actors.messages

import pl.edu.pw.elka.sag.tweets.Sentiment
import weka.core.Attribute

object Messages {
  case class BuildModel()
  case class PrepareTweetInstance(sentiment: Sentiment, text: String, att: Attribute)
  case class StartTweetClassification()
}
