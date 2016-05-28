package pl.edu.pw.elka.sag.actors.classify

import akka.actor.ActorRef
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.classify.Messages.PredictCandidate
import pl.edu.pw.elka.sag.classification.setup.DenseInstanceBuilder
import pl.edu.pw.elka.sag.weka.Weka
import weka.classifiers.Classifier
import weka.filters.Filter

class SingleCandidatePopularityPredictingActor extends MasterActor {
  override def workers: ActorRef = ???

  override def receive: Receive = {
    case PredictCandidate(model, file) =>
      val filter: Filter = model.filter
      val classifier: Classifier = model.classifier
      val weka = new Weka()
      val dib = new DenseInstanceBuilder();

      val tweets = io.Source.fromFile(file)
      tweets.getLines().foreach(tweet => {
        val instances = weka.prepareInstances()
        val instance = dib.buildDenseInstanceFromColumns(instances.attribute(0), dib.getColumnsFromText(tweet))
        instances.add(instance)
        val filteredInstances = weka.filter(instances, filter)
        val result: Double = classifier.classifyInstance(filteredInstances.get(0))
        println(s"Wynik: $result dla $tweet")
      })
      tweets.close()
  }

  override def finishCurrentActor(): Unit = ???
}
