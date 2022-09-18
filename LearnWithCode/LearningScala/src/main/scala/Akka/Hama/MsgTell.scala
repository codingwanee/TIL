package akka.hama

import HamaCode.ActorsHierarchy.ourSystem
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging


/*
  말하기
  - ! 연산자 사용. 기본적인 연산이며, 비블로킹 연산
  - fire and forget. 메시지를 보낸 다음 책임지지 않음
  - 많아야 한 번만 배달을 보장
  - 메세지 배달 순서 보장

  아래 소스는
  1) Pingy ----"ping"----> Pongy
  2) Pingy <---"pong"----- Pongy (sender에게 되돌려주기)
 */

/*
  lazy val : 초기화 표현식을 바로 계산하지 않고 프로그램 내에서 그 val 값을 처음 사용할 때 계산
            처음 계산한 값을 보관해두고, 나중에 같은 val을 다시 사용하면 그때 저장했던 값을 재사용
            즉, val은 변수가 선언될 당시 실행 / lazy val은 해당 변수에 접근할 때 실행

*/

class Pongy extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "ping" =>
      log.info("Got a ping -- ponging back!")
      sender ! "pong" // sender에게 "pong"이라고 메시지 보내기
      context.stop(self)
  }

  override def postStop() = log.info("pongy going down")
}


class Pingy extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case pongyRef: ActorRef =>
      pongyRef ! "ping"
    case "pong" =>
      log.info("got a pong back!")
      context.stop(self)
  }
  override def postStop() = log.info("ping going down")
}

class Master extends Actor {
  val log = Logging(context.system, this)
  val pingy = ourSystem.actorOf(Props[Pingy], "pingy")
  val pongy = ourSystem.actorOf(Props[Pongy], "pongy")

  def receive = {
    case "start" =>
      pingy ! pongy
  }
  override def postStop() = log.info("master going down")
}

object Communicating extends App {
  val masta = ourSystem.actorOf(Props[Master], "masta")
  masta ! "start"
  Thread.sleep(5000)
  ourSystem.terminate()
}