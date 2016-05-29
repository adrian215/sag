package pl.edu.pw.elka.sag.actors.classify

import akka.actor.{ActorRef, Props}
import akka.routing.RoundRobinPool
import pl.edu.pw.elka.sag.actors.MasterActor
import pl.edu.pw.elka.sag.actors.classify.Messages.{CannotClassifyTweet, ClassifyTweet, PredictCandidate, TweetClassified}
import pl.edu.pw.elka.sag.classification.Model
import pl.edu.pw.elka.sag.config.ApplicationConfig
import weka.classifiers.{AbstractClassifier, Classifier}

class SingleCandidatePopularityPredictingActor extends MasterActor {
  override protected def workers: ActorRef = context.actorOf(RoundRobinPool(ApplicationConfig.actorSize).props(Props[TweetClassificationActor]))

  private val candidateVoter: CandidateVoter = new CandidateVoter

  override def receive: Receive = {
    case PredictCandidate(model, file) =>
      predicateCandidate(model, file)
    case TweetClassified(response) =>
      childActorFinished()
      candidateVoter.vote(response.sentiment)
    case CannotClassifyTweet => childActorFailed()
  }

  private def predicateCandidate(model: Model, file: FileName): Unit = {
    val tweets = io.Source.fromFile(file)
    tweets.getLines().foreach { tweet =>
      val classifyTweet = ClassifyTweet(makeCopy(model), tweet)
      spawnChildWithMessage(classifyTweet)
    }
    tweets.close()
  }

  private def makeCopy(model: Model): Model = {
    val copy: Classifier = AbstractClassifier.makeCopy(model.classifier)
    model.copy(classifier = copy)
  }

  override protected def finishCurrentActor(): Unit = {
    println(s"Poparcie na podstawie tweetow: ${candidateVoter.getResult}")
  }
}
