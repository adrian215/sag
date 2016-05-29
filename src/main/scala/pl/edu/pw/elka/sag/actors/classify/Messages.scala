package pl.edu.pw.elka.sag.actors.classify

import pl.edu.pw.elka.sag.classification.{TweetClassificationResponse, Model}

object Messages {

  case class StartTweetClassification()
  case class PredictCandidate(model: Model, fileName:FileName)
  case class ClassifyTweet(model: Model, tweet: Tweet)
  case class TweetClassified(tweetClassificationResponse: TweetClassificationResponse)
  case class CannotClassifyTweet()
}
