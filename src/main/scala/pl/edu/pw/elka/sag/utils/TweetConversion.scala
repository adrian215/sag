package pl.edu.pw.elka.sag.utils

object TweetConversion {

  implicit def toTweetText(text: String): TweetText = {
    new TweetText(text)
  }
}
