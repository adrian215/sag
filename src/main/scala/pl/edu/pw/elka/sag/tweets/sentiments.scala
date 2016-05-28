package pl.edu.pw.elka.sag

package object sentiments {
  sealed trait Sentiment {
    val value: Int
  }

  case object POSITIVE extends Sentiment {
    override val value: Int = 1
  }

  case object NEGATIVE extends Sentiment {
    override val value: Int = 0
  }
}
