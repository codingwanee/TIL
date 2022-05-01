package HamaCode

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

// 부모 액터
class ParentActor extends Actor {
  val log = Logging(context.system, this)

  def receive = {

    // "create" 메세지를 받으면 context 이용하여 자식 액터 생성.
    case "create" =>
      context.actorOf(Props.apply(classOf[ChildActor], "greeting"))
      // log.info(s"created a new child - children = ${context.children}")

    // "hi"를 받으면 자식 액터들에게 "hi" 전달. context에 자식들의 참조가 있다.
    case "hi" =>
      log.info("Kids, say hi!")
      for (c <- context.children) c ! "hi"

    // "stop" 메세지를 받으면 부모 액터 종료. ===> 자동적으로 모든 자식들도 종료
    case "stop" =>
      log.info("parent stopping")
      context.stop(self)
  }
}

// 자식 액터
class ChildActor(message: String) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    // hi 메세지 받으면 context에서 부모참조 찾아서 부모이름을 입력
    case "hi" =>
      val parent = context.parent
      log.info(s"my parent $message $parent made me say hi!")
  }

  // postStop 메소드 오버라이드. 액터가 종료되기 전에 호출됨
  override def postStop(): Unit = {
    log.info("child stopped!")
  }
}

// 프로젝트 시작
object ActorsHierarchy extends App {
  // 액터 시스템 생성. 이름은 mysystem
  val ourSystem = ActorSystem("mysystem")

  // 부모 액터 생성 <- 이미 존재하는 부모액터의 인스턴스겠지?
  val parent = ourSystem.actorOf(Props[ParentActor], "parent")
  parent ! "create"
  parent ! "create"
  Thread.sleep(1000)
  parent ! "hi"
  Thread.sleep(1000)
  parent ! "stop"
  Thread.sleep(1000)
  ourSystem.terminate()
}

