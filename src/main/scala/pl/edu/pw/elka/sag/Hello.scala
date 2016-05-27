package pl.edu.pw.elka.sag

import akka.actor.{ActorRef, Props, ActorSystem}

import java.util

import weka.core.{Attribute, DenseInstance, FastVector, Instances}

object Hello {

  def main(args: Array[String]) {




  }

    val actor: ActorRef = ActorSystem("Example").actorOf(Props[ExampleActor], name = "test")
    actor ! "hello"
    actor ? "pytanie"
}
