package HamaCode

import HamaCode.ActorsHierarchy.ourSystem
import akka.actor.{Actor, ActorRef, Props}
import akka.event.Logging
import akka.pattern.{ask, pipe}
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/*
  물어보기(ask)
  - ? 연산자 사용
  - 비블로킹 연산
  - Future를 통해서 ask 메세지를 돌려받는다.

  tell과 ask의 차이
  - tell: 메시지를 전송하고 바로 결과를 반환 (비동기) <--- 성능적 장점
  - ask : 결과를 Future로 감싸서 반환     (비동기) <--- 필요한 상황에서 사용
          Future 객체는 인자로 받은 시간 이내에 Pongy로부터 메세지를 전달받으면 Master에게 전달

  예제소스 내용
  pingy ----"ping"----> pongy
  pingy <--(("ping"))--- pongy

 */


class PongyAsk extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case "ping" =>
      log.info("Got a ping -- ponging back!")
      sender ! "pong"
      context.stop(self)
  }
  override def postStop() = log.info("pongy going down")
}

class PingyAsk extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case pongyRef: ActorRef =>
      implicit val timeout = Timeout(2 seconds) // implicit : 암시
      pongyRef.ask()
      val future = pongyRef ? "ping" // ? 연산자로 메시지 전송
      pipe(future) to sender   // Future 객체로 내용을 감싸서 전달
      // future onComplete { case v => log.info(s"Response : $v") } 이렇게도 가능하나
      // onComplete 핸들러 안에서 sender 메소드를 호출해서도 안된다.
  }
  override def postStop() = log.info("Pingy going down")
}

import scala.concurrent.Future
object My{
  def myfunc(): Future[String] = ???
  def another(): Future[Int] = ???
}

case class Person(name: String, age: Int)

class MasterAsk extends Actor {


  val log = Logging(context.system, this)
  val pingy = ourSystem.actorOf(Props[PingyAsk], "pingy")
  val pongy = ourSystem.actorOf(Props[PongyAsk], "pongy")
  def receive = {
    case "start" =>
      pingy ! pongy
    case "pong" =>
      log.info("got a pong back!")
      context.stop(self)
  }
  override def postStop() = log.info("master going down")
}

object CommunicatingAsk extends App {
  val masta = ourSystem.actorOf(Props[MasterAsk], "masta")
  masta ! "start"
  Thread.sleep(1000)
  ourSystem.terminate()
}