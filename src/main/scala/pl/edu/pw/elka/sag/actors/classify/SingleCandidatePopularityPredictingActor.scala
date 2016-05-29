package pl.edu.pw.elka.sag.actors.classify

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.classify.Messages.PredictCandidate
import pl.edu.pw.elka.sag.classification.{DenseInstanceBuilder, Model}
import pl.edu.pw.elka.sag.tweets.TweetConversions.doubleToSentiment
import pl.edu.pw.elka.sag.weka.Weka
import weka.classifiers.{AbstractClassifier, Classifier}
import weka.core.Instances
import weka.filters.Filter

import scala.io.BufferedSource

class SingleCandidatePopularityPredictingActor extends Actor{

  private val candidateVoter: CandidateVoter = new CandidateVoter
  val weka = new Weka()
  val dib = new DenseInstanceBuilder()

  override def receive: Receive = {
    case PredictCandidate(model, file) =>
      predicateCandidate(makeCopy(model), file)
  }

  private def predicateCandidate(model: Model, file: FileName): Unit = {
    val tweets = io.Source.fromFile(file)

    classifyTweets(model, tweets)

    tweets.close()
  }

  def classifyTweets(oldModel: Model, tweets: BufferedSource): Unit = {
    val model = makeCopy(oldModel)
    val filter: Filter = model.filter
    val classifier: Classifier = model.classifier
    val instances = weka.prepareInstances()

    tweets.getLines().map(tweet =>
      dib.buildDenseInstance(tweet, instances.attribute(0))
    ).flatten.foreach(instances.add(_))

    val filteredInstances: Instances = weka.filter(instances, filter)

    (0 until filteredInstances.size())
      .map(filteredInstances.get(_))
      .map(classifier.classifyInstance(_))
      .foreach(candidateVoter.vote(_))

    println(s"\nPoparcie na podstawie tweetow: ${candidateVoter.getResult}")

  }

  private def makeCopy(model: Model): Model = {
    val classifierCopy: Classifier = AbstractClassifier.makeCopy(model.classifier)
    val filterCopy: Filter = Filter.makeCopy(model.filter)
    model.copy(classifier = classifierCopy, filter = filterCopy)
  }
}
