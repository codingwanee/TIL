package akka.quartz

import akka.actor.{ActorRef, Props}
import akka.hama.ActorsCreate.mySystem

object SendingService extends App {

  val myactor: ActorRef = mySystem.actorOf(Props[ReceiverActor])

  myactor ! "test"
  Thread.sleep(1000)

  myactor ! "hi"
  Thread.sleep(1000)

  mySystem.terminate()

}
