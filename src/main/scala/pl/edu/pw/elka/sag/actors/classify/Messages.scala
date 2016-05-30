package pl.edu.pw.elka.sag.actors.classify

import pl.edu.pw.elka.sag.model.{CandidatePopularity, TweetClassificationResponse, AlgorithmModel}

object Messages {

  case class StartTweetClassification()
  case class PredictCandidate(candidate: Candidate, model: AlgorithmModel, fileName:FileName)
  case class CandidatePredicted(candidatePopularity: CandidatePopularity)

  case class ClassifyTweet(model: AlgorithmModel, tweet: Tweet)
  case class TweetClassified(tweetClassificationResponse: TweetClassificationResponse)
  case class CannotClassifyTweet()
}
