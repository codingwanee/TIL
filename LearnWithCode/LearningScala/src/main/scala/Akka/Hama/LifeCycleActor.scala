package HamaCode

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

/*
  액터의 생명주기
  - 액터의 생명주기는 생성, 재생성, 종료가 있다.
  - 재생성: 액터시스템 어디선가 에러가 발생했을 때 다시 시작하는 것

  preStart    : 생성자가 완료된 직후에 실행
  preStop     : 액터의 라이프사이클이 완전히 종료하기 직전에 실행
  preRestart  : 재시작이 이루어지기 직전에 실행
  postRestart : 재시작이 이루어진 직후에 실행
 */

class LifeCycleActor extends Actor {

  val log = Logging(context.system, this) // val은 상수 선언
  var child: ActorRef = _                 // var는 변수 선언


  def receive = {
    case num: Double => log.info(s"got a double - $num")
    case num: Int  => log.info(s"got an integer - $num")
    case lst: List[_] => log.info(s"list - ${lst.head}, ...")
    case txt: String => child ! txt

  }

  override def preStart(): Unit= {
    log.info("about to start")
    child = context.actorOf(Props[MessagePrinter], "kiddo")
  }

  override def preRestart(reason: Throwable, msg: Option[Any]): Unit = {
    log.info(s"about to restart because of $reason, during message $msg")
    super.preRestart(reason, msg)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info(s"just restarted due to $reason")
    super.postRestart(reason)
  }

  override def postStop() = log.info("just stopped")

}


class MessagePrinter extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case msg => log.info(s"child got message '$msg'")
  }
}
