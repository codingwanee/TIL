package HamaCode

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.Logging

/*
  ** 액터 식별 **
  - 파일 시스템의 파일 경로와 매우 비슷
  - ../ * 현재 액터와 모든 형제들을 의미
  - 액터 경로만을 사용해 특정 액터에 메세지를 보낼 수 '없다'.
  - 액터 경로에 있는 액터 참조를 얻으려면 context 객체에 대해 actorSelection 메소드를 호출
  - 어떤 아카 액터가 Identify 메세지를 받으면, 자동으로 자신의 ActorRec가 들어있는 ActorIdentify 메세지로 응답
  - 액터 선택이 가리키는 액터가 없으면, 아무 ActorRef 객체가 없는 ActorIdentify 메세지를 송신한 액터에 돌려줌
    ---> 송신한 액터는 case ActorIdentify(path, None)으로 받음
 */

class CheckActor extends Actor {
  import akka.actor.{Identify, ActorIdentity}

  val log = Logging(context.system, this)

  def receive = {
    case path: String =>
      log.info(s"checking path => $path")
      context.actorSelection(path) ! Identify(path)
    case ActorIdentity(path, Some(ref)) =>
      log.info(s"found actor $ref on $path")
    case ActorIdentity(path, None) =>
      log.info(s"could not find an actor on $path")

  }
}

object ActorsIdentify extends App {
  val ourSystem = ActorSystem("mysystem")

  val checker = ourSystem.actorOf(Props[CheckActor], "checker")

  checker ! "../*"          // 현재 액터 부모의 아래에 있는 모든 자식 액터는? --> 현재 액터 뿐
  Thread.sleep(1000)
  checker ! "../../*"       // 현재 액터 조부모의 아래에 있는 모든 자식 액터는? --> system, user 액터
  Thread.sleep(1000)
  checker ! "/system/*"     // system 액터의 모든 자식 액터는?
  Thread.sleep(1000)
  checker ! "/user/checker2"  // user 액터 자식 중에서 checker2 액터는?
  Thread.sleep(1000)
  checker ! "akka://OurExampleSystem/system"
  Thread.sleep(1000)
  ourSystem.stop(checker)
  Thread.sleep(1000)
  ourSystem.terminate()

}