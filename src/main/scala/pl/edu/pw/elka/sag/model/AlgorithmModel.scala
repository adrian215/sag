package pl.edu.pw.elka.sag.model

import weka.classifiers.Classifier
import weka.filters.Filter

case class AlgorithmModel(classifier: Classifier, filter: Filter)
