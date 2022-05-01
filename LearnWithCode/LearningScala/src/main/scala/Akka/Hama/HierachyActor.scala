package HamaCode

import HamaCode.ActorsHierarchy.ourSystem
import HamaCode.ChildActor
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor.{Actor, ActorKilledException, Kill, OneForOneStrategy, Props}
import akka.event.Logging


/*
  액터의 관리 (고장/예외처리)
 - Supervisor 관리액터는 아무 메세지를 처리하지 않고 하위 액터가 오류가 생겼을 때 전략을 세운다.
 - 여기선 OneForOneStrategy 라고 하나의 액터에 대해서 처리를 정의
 - AllForOneStrategy는 모두에게 연대책임을 묻는다 (즉, 말썽을 부린 액터의 형제/자매 액터들에게) 한꺼번에 적용
 */



// 부모 액터
class SupervisorActor extends Actor {
  val child = context.actorOf(Props[ChildrenActor], "victim")

  def receive = PartialFunction.empty

  override val supervisorStrategy = {
    // OneForOneStrategy : 하나의 액터에 대해서 처리를 정의
    // AllForOneStrategy : 모두에게 연대책임 (즉, 문제가 된 액터의 형제/자매 액터들에게 한꺼번에 적용 가능)
    OneForOneStrategy() {
      case ake: ActorKilledException => Restart // 자식 액터에서 ActorKilledException 예외 발생시 Restart로 재시작
      case _ => Escalate  // 기타 예외는 자신의 부모 액터에게 책임 넘기기
    }
  }

}

// 자식 액터
class ChildrenActor extends Actor {
  val log = Logging(context.system, this)

  // String이 아닌 메세지는 RuntimeException 발생
  def receive = {
    case s: String => log.info(s)
    case msg => throw new RuntimeException
  }

}


object SupervisionKill extends App {
  val s = ourSystem.actorOf(Props[SupervisorActor], "super")

  ourSystem.actorSelection("/user/super/*") ! Kill
  ourSystem.actorSelection("/user/super/*") ! "sorry about that"
  ourSystem.actorSelection("/user/super/*") ! "kaboom".toList
  Thread.sleep(1000)
  ourSystem.stop(s)
  Thread.sleep(1000)
  ourSystem.terminate()
}
