package pl.edu.pw.elka.sag.classification

import weka.classifiers.Classifier
import weka.filters.Filter

case class Model(classifier: Classifier, filter: Filter)
