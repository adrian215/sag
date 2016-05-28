package pl.edu.pw.elka.sag.actors.classify

import pl.edu.pw.elka.sag.classification.Model

object Messages {

  case class StartTweetClassification()
  case class PredictCandidate(model: Model, fileName:String)
}
