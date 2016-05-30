package pl.edu.pw.elka.sag.actors.createmodel

import pl.edu.pw.elka.sag.model.AlgorithmModel
import weka.core.{Attribute, DenseInstance}

object Messages {
  case class BuildModel()
  case class ModelCreated(model: AlgorithmModel)

  case class PrepareTweetInstance(text: String, att: Attribute)
  case class TweetInstanceCreated(instance: DenseInstance)
  case class CannotCreateTweetInstance()
}
