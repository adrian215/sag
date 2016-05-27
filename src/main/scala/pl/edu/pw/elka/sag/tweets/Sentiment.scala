package pl.edu.pw.elka.sag.tweets

object Sentiment extends Enumeration{
  sealed trait SentimentValue
  case class Positive(value: Int = 1) extends SentimentValue
  case class Negative(value: Int = 0) extends SentimentValue
}
