package akka.Hama

import akka.actor.{Actor, Identify, Props}

class Runner extends Actor {
  val pingy = context.actorOf(Props[Pingy], "pingy")

  def receive = {
    case "start" =>
      val path = context.actorSelection("akka.tcp://PongyDimension@192.168.35.57:24321/user/pongy") // 보낼 주소
      path ! Identify(0)
  }
}


