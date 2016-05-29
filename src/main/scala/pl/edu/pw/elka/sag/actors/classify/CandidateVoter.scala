package pl.edu.pw.elka.sag.actors.classify

import pl.edu.pw.elka.sag.tweets.{NEGATIVE, POSITIVE, Sentiment}

class CandidateVoter {
  private var allVotes, positiveVotes: Votes = 0

  def vote(sentiment: Sentiment): Unit = sentiment match {
    case POSITIVE =>
      incrementAllVotes
      incrementPositive
    case NEGATIVE => incrementAllVotes
  }

  private def incrementPositive: Unit = {
    positiveVotes += 1
  }

  private def incrementAllVotes: Unit = {
    allVotes += 1
  }

  def getResult: CandidatePopularity = {
    positiveVotes.toDouble / allVotes.toDouble
  }
}
