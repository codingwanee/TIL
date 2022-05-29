package HamaCode

import HamaCode.ActorsHierarchy.ourSystem
import akka.actor.{Actor, Props}
import akka.event.Logging


/*
  전달하기 (Router)
  - 오직 메세지를 다른 액터에게 넘겨주기 위해서만 사용
  - 예를들어 라운드 로빈 방식의 로드밸런싱같은 느낌
  - forward 메소드 이용
  - Router 액터는 자식 액터에게 균등하게 msg를 포워딩한다
 */

class StringPrinter extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case msg => log.info(s"child got message '$msg'")
  }

  override def preStart(): Unit = log.info(s"child about to start.")
  override def postStop(): Unit = log.info(s"child just stopped.")
}


class MsgRouter extends Actor {
  var i = 0
  var children = for (_ <- 0 until 4) yield context.actorOf(Props[StringPrinter])
  def receive = {
    case "stop" => context.stop(self)
    case msg =>
      children(i) forward msg // forward 메소드로 균등하게 msg 포워딩
      i = (i + 1) % 4
  }
}

object CommunicatingRouter extends App {
  val router = ourSystem.actorOf(Props[MsgRouter], "router")
  router ! "Hi."
  router ! "I'm talking to you!"
  Thread.sleep(1000)
  router ! "stop"
  Thread.sleep(1000)
  ourSystem.terminate()
}