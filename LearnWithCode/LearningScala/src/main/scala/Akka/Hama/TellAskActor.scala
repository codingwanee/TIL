package Akka.Hama

import Akka.Hama.mysystem.ourSystem
import akka.actor._
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


object mysystem {
  lazy val ourSystem = ActorSystem("ourSystem")
}

class Pongy extends Actor {

  def receive = {
    case "ping" =>
      println("pong!")
      sender ! "pong"
      context.stop(self)
  }
  override def postStop = {
    println("pongy stop . . .")
  }
}

class Pingy extends Actor {

  def receive = {

    case pongyRef: ActorRef =>
      // ! (tell)로 송신
      // pongyRef ! "ping"

      // ? (ask)로 송신
      implicit val timeout = Timeout(2 seconds)
      val future = pongyRef ? "ping"
      pipe(future) to sender()
    case "pong" =>
      println("ping!")
      context.stop(self)
  }
  override def postStop() = {
    println("pingly stop . . .")
  }
}


class Master extends Actor {
  val pingy = ourSystem.actorOf(Props[Pingy], "pingy")
  val pongy = ourSystem.actorOf(Props[Pongy], "pongy")

  def receive = {
    case "start" =>
      pingy ! pongy
  }

  override def postStop(): Unit = println("Master stop . . .")
}


object TellAskRouterActor extends App {
  val masta = ourSystem.actorOf(Props[Master], "masta")
  masta ! "start"
  Thread.sleep(5000)
  ourSystem.terminate()
}