package pl.edu.pw.elka.sag.actors.classify

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.classify.Messages.{CandidatePredicted, PredictCandidate}
import pl.edu.pw.elka.sag.model.TweetConversions.doubleToSentiment
import pl.edu.pw.elka.sag.model.{AlgorithmModel, CandidatePopularity}
import pl.edu.pw.elka.sag.utils.{CandidateVoter, DenseInstanceBuilder}
import pl.edu.pw.elka.sag.weka.Weka
import weka.classifiers.{AbstractClassifier, Classifier}
import weka.core.{Instance, Instances}
import weka.filters.Filter

import scala.io.BufferedSource

class SingleCandidatePopularityPredictingActor extends Actor{

  val weka = new Weka()
  val dib = new DenseInstanceBuilder()

  override def receive: Receive = {
    case PredictCandidate(candidate, model, file) =>
      predicateCandidate(candidate, makeCopy(model), file)
  }

  private def makeCopy(model: AlgorithmModel): AlgorithmModel = {
    val classifierCopy: Classifier = AbstractClassifier.makeCopy(model.classifier)
    val filterCopy: Filter = Filter.makeCopy(model.filter)
    model.copy(classifier = classifierCopy, filter = filterCopy)
  }

  private def predicateCandidate(candidate: Candidate, model: AlgorithmModel, file: FileName): Unit = {
    val tweets = io.Source.fromFile(file)
    val popularity: Popularity = classifyTweets(model, tweets)
    tweets.close()
    sender ! CandidatePredicted(CandidatePopularity(candidate, popularity))
  }

  private def classifyTweets(model: AlgorithmModel, tweets: BufferedSource): Popularity = {
    val filter: Filter = model.filter
    val classifier: Classifier = model.classifier

    val preparedTweets: Instances = prepareTweetToClassification(tweets, filter)
    classifyPreparedTweets(classifier, preparedTweets)
  }

  private def prepareTweetToClassification(tweets: BufferedSource, filter: Filter): Instances = {
    val instances = weka.prepareInstances()
    populateInstancesWithTweets(tweets, instances)
    weka.filter(instances, filter)
  }

  private def classifyPreparedTweets(classifier: Classifier, preparedTweets: Instances): Popularity = {
    val candidateVoter: CandidateVoter = new CandidateVoter

    each(preparedTweets)
      .map(classifier.classifyInstance)
      .foreach(candidateVoter.vote(_))

    candidateVoter.printStats
    candidateVoter.getResult
  }

  private def populateInstancesWithTweets(tweets: BufferedSource, instances: Instances): Unit = {
    tweets.getLines().map(tweet =>
      dib.buildDenseInstance(tweet, instances.attribute(0))
    ).flatten.foreach(instances.add(_))
  }

  private def each(filteredInstances: Instances): IndexedSeq[Instance] = {
    (0 until filteredInstances.size())
      .map(filteredInstances.get)
  }
}
