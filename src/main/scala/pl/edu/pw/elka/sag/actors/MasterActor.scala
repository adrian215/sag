package pl.edu.pw.elka.sag.actors
import akka.actor.{Actor, ActorRef}
import pl.edu.pw.elka.sag.actors.createmodel.Messages
import Messages.PrepareTweetInstance

trait MasterActor extends Actor{

  private var allWorkers: Int = 0
  private var currentWorked = 0
  protected def workers: ActorRef

  final protected def spawnChildWithMessage(instance: PrepareTweetInstance): Unit = {
    allWorkers += 1
    println(s"Sending message $instance to child actor")
    workers ! instance
  }

  final protected def childActorFinished(): Unit = {
    currentWorked += 1
    println(s"Finished $currentWorked / $allWorkers")
    if (allChildrenFinished) {
      finishCurrentActor()
    }
  }

  private def allChildrenFinished: Boolean = {
    currentWorked == allWorkers
  }

  protected def finishCurrentActor(): Unit
}
