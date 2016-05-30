package pl.edu.pw.elka.sag.actors.classify

import akka.actor.Actor
import pl.edu.pw.elka.sag.actors.classify.Messages.{TweetClassified, CannotClassifyTweet, ClassifyTweet}
import pl.edu.pw.elka.sag.utils.DenseInstanceBuilder
import pl.edu.pw.elka.sag.model.{TweetClassificationResponse, Sentiment}
import pl.edu.pw.elka.sag.weka.Weka
import weka.classifiers.Classifier
import weka.core.{Instances, DenseInstance}
import weka.filters.Filter

import pl.edu.pw.elka.sag.model.TweetConversions.doubleToSentiment

/**
  * @deprecated Aktor odpowiedzialny za danego kandydata sklasyfikuje wszystkie tweety
  */
@Deprecated
class TweetClassificationActor extends Actor {

  val weka = new Weka()
  val dib = new DenseInstanceBuilder()

  override def receive: Receive = {
    case ClassifyTweet(model, tweet) =>
      val filter: Filter = model.filter
      val classifier: Classifier = model.classifier
      val instances = weka.prepareInstances()

      val instance: Option[DenseInstance] = dib.buildDenseInstance(tweet, instances.attribute(0))

      val responseMessage = instance.map { instance =>
        val result = classifyTweet(filter, classifier, instances, instance)
        //        println(s"Wynik: $result dla $tweet")
        val response = TweetClassificationResponse(result)
        TweetClassified(response)
      } getOrElse CannotClassifyTweet

      sender ! responseMessage
  }

  private def classifyTweet(filter: Filter, classifier: Classifier, instances: Instances, instance: DenseInstance): Sentiment = {
    instances.add(instance)
    val filteredInstances = weka.filter(instances, filter)
    classifier.classifyInstance(filteredInstances.get(0))
  }
}
