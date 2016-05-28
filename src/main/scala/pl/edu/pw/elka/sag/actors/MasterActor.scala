package pl.edu.pw.elka.sag.actors
import akka.actor.{Actor, ActorRef}
import pl.edu.pw.elka.sag.actors.createmodel.Messages
import Messages.PrepareTweetInstance

trait MasterActor extends Actor{

  var allWorkers: Int = 0
  var currentWorked = 0
  def workers: ActorRef

  def spawnChildWithMessage(instance: PrepareTweetInstance): Unit = {
    allWorkers += 1
    println(s"Sending message ${instance} to child actor")
    workers ! instance
  }

  def childActorFinished(): Unit = {
    currentWorked += 1
    println(s"Finished $currentWorked / $allWorkers")
    if (allChildrenFinished) {
      finishCurrentActor()
    }
  }

  def allChildrenFinished: Boolean = {
    currentWorked == allWorkers
  }

  def finishCurrentActor(): Unit
}
