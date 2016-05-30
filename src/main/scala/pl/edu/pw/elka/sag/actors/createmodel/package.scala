package pl.edu.pw.elka.sag.actors

import pl.edu.pw.elka.sag.model.AlgorithmModel

package object createmodel {
  type ModelCreated = (AlgorithmModel) => Unit
}
