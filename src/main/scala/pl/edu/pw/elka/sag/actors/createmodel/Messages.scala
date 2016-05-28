package pl.edu.pw.elka.sag.actors.createmodel

import pl.edu.pw.elka.sag.classification.Model
import pl.edu.pw.elka.sag.sentiments.Sentiment
import weka.core.{Attribute, DenseInstance}

object Messages {
  case class BuildModel()
  case class ModelCreated(model: Model)

  case class PrepareTweetInstance(sentiment: Sentiment, text: String, att: Attribute)
  case class TweetInstanceCreated(instance: DenseInstance)
  case class CannotCreateTweetInstance()
}
