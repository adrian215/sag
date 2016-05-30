package pl.edu.pw.elka.sag.actors
import akka.actor.{Actor, ActorRef}
import pl.edu.pw.elka.sag.actors.createmodel.Messages
import Messages.PrepareTweetInstance

trait MasterActor extends Actor{

  private var allWorkers: Int = 0
  private var currentWorked = 0
  private var failed = 0
  protected def workers: ActorRef

  final protected def spawnChildWithMessage(message: Any): Unit = {
    allWorkers += 1
    showStatus
    workers ! message
  }

  final protected def childActorFinished(): Unit = {
    currentWorked += 1
    showStatus
    if (allChildrenFinished) {
      finishCurrentActor()
    }
  }

  protected def finishCurrentActor(): Unit

  private def allChildrenFinished: Boolean = {
    currentWorked == allWorkers
  }

  final protected def childActorFailed(): Unit = {
    failed += 1
    childActorFinished()
  }

  def showStatus: Unit = {
    print(s"\rWorking childs: $currentWorked / $allWorkers, failed: $failed")
  }
}
