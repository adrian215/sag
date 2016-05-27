package pl.edu.pw.elka.sag

package object tweets {
  sealed trait Sentiment {
    val value: Int
  }

  case object POSITIVE extends Sentiment {
    override val value: Int = 1
  }

  case object NEGATIVE extends Sentiment(0) {
    override val value: Int = 0
  }
}
