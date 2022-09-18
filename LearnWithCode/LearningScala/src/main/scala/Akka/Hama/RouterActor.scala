package akka.Hama

import akka.Hama.mysystem.ourSystem
import akka.actor.{Actor, Props}


class StringPrinter extends Actor {
  def receive = {
    case msg =>
      println(s"got msg : $msg")
  }
}

class RouterActor extends Actor {

  // 자식 액터를 4개 생성하여 children에 시퀀스로 저장
  var i = 0
  var children = for (_ <- 0 until 4) yield context.actorOf(Props[StringPrinter])

  //
  def receive = {
    case "stop" =>
      context.stop(self)
    case msg =>
      // 자식 액터에게 균등하게 포워딩
      children(i) forward msg
      i = (i+1) % 4
  }

}


object CommunicatingRouter extends App {
  val router = ourSystem.actorOf(Props[RouterActor], "router")

  router ! "hi"
  router ! "sending message from CommunicatingRouter"
  Thread.sleep(1000)
  router ! "now stop app"
  ourSystem.terminate()
}