package pl.edu.pw.elka.sag.tweets

sealed trait Sentiment {
  def value: Int
}

case object POSITIVE extends Sentiment {
  override val value: Int = 1
}

case object NEGATIVE extends Sentiment {
  override val value: Int = 0
}
