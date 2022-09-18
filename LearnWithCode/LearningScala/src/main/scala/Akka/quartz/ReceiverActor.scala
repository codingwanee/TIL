package akka.quartz

import akka.actor.{Actor, ActorSystem, Props}

class ReceiverActor extends Actor {
  override def receive: Receive = {
    case "test" =>
      println("got test msg from quartz scheduling")
    case _ =>
      println("fired schedule but not the exact msg")
  }
}


