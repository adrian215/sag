package pl.edu.pw.elka.sag.utils

class TweetText(val text: String) {
  val EMPTY: String = ""

  def removeHashtag(): TweetText = {
    val newText: String = text.replaceAll("#\\S*", EMPTY)
    new TweetText(newText)
  }

  def removePerson(): TweetText = {
    val newText: String = text.replaceAll("@\\S*", EMPTY)
    new TweetText(newText)
  }

  def removeUrl(): TweetText = {
    val newText: String = text.replaceAll("https?://\\S+\\s?", EMPTY)
    new TweetText(newText)
  }

  implicit override def toString: String = text
}
