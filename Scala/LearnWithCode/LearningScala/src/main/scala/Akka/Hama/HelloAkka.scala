package Akka.Hama

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

// 나의 액터 클래스
class HelloAkka(val hello: String) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case "hello" =>
      log.info(s"Received message $hello !")
    case msg =>
      log.info(s"Unexpected message '$msg'")
      context.stop(self)
  }
}

object ActorsCreate extends App {

  // 액터 시스템 생성. 이름은 mysystem
  val mySystem = ActorSystem("mysystem")

  // 액터 시스템을 통하여 나의 Actor 생성. 이름은 greeter 타입은 ActorRef
  val hiActor : ActorRef = mySystem.actorOf(Props(new HelloAkka("hi")), name = "greeter")

  // 나의 액터와 교신. "hi"라는 메세지를 보낸다. // Received message hi! 출력
  hiActor ! "hi"
  Thread.sleep(1000)
  hiActor ! 3
  Thread.sleep(1000)

  // 액터 시스템 종료
  mySystem.terminate()

  /*
    App 이라는 trait를 상속받으면 main 메소드에서 실행하는 효과


   */


}
