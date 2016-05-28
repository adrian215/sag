package pl.edu.pw.elka.sag.actors

import pl.edu.pw.elka.sag.classification.Model

package object createmodel {
  type ModelCreated = (Model) => Unit
}
