package pl.edu.pw.elka.sag.utils

import pl.edu.pw.elka.sag.actors.classify._
import pl.edu.pw.elka.sag.model.{NEGATIVE, POSITIVE, Sentiment}

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

  def getResult: Popularity = {
    positiveVotes.toDouble / allVotes.toDouble
  }

  def printStats: Unit ={
    println(s"\nPoparcie na podstawie tweetow: ${getResult}\n" +
      s"pozytywnych: $positiveVotes, negatywnych: ${allVotes - positiveVotes}, wszystkich: $allVotes")
  }
}
