package pl.edu.pw.elka.sag.tweets

object TweetConversions {

  implicit def toTweetText(text: String): TweetText = {
    new TweetText(text)
  }

  implicit def TweetTextToString(tweetText: TweetText): String = tweetText.toString

  implicit def doubleToSentiment(double: Double): Sentiment = double match {
    case 1 => POSITIVE
    case 0 => NEGATIVE
  }
}
