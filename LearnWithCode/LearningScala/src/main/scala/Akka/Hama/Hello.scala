package HamaCode

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

/*
  1) 액터 시스템 생성
  2) 액터 시스템에 우리가 만든 액터클래스 객체화하여 등록

  - 액터 클래스에는 receive라는 메소드가 있는데, 액터는 이 receive 메소드를 통하여 메일박스로부터 받은 메시지 처리
  - receive 메소드는 PartialFunction[Any, Unit] 타입의 객체 >> Any 타입의 메세지를 받아서 처리
  - Props 객체는 액터 인스턴스를 만드는데 필요한 모든 정보를 캡슐화해 놓았다. (직렬화 가능하며, 네트워크를 통해 전송 가능)
 */

class Hello(val hello: String) extends Actor {
  val log=Logging(context.system, this)

  // 메시지 수신
  // Actor안에 있는 receive 메소드를 통하여 액터는 메일박스로부터 받은 메시지 처리
  // receive는 PartialFunction[Any, Unit] 타입의 객체. Any 타입의 메세지를 받아서 처리
  def receive = {
    case `hello` => // 받은 메세지가 'hello'일 때
      log.info(s"Received a $hello!!!")
    case msg => // 메세지를 변수로 받아왔을 때
      log.info(s"Unexpected message '$msg'")
      context.stop(self)

    // 클래스를 케이스 매칭하는 경우 예시
      // case ClassOne(x, y) => prinln(s"Recieved $x and $y")
      // case ClassTwo(a, Some(ClassOne(x, y)) if a == 42 => // do something
      // case c @ ClassOne(_, "foo") => // only match if y == "foo", now c is your instance of ClassOne
  }
}


// App : The `App` trait can be used to quickly turn objects into executable programs.

object ActorsCreate extends App {

  // 액터 시스템 생성. 이름은 mysystem
  // ActorSystem : Akka 프레임워크에 대한 참조이다. 모든 액터들은 액터시스템의 context 내에서 동작.
  //               ActorSystem의 context에서 첫 번째 액터들을 만들어야 함
  //               ActorSystem은 각 어플리케이션에 하나만 생성(무거운 객체)
  val mySystem = ActorSystem("mysystem")

  // 액터시스템을 통하여 나의 Actor 생성. 이름은 greeter 타입은 ActorRef
  // ActorRef :
  // Props : 주어진 종류의 액터를 만들기 위해 캡슐화된 객체
  val hiActor : ActorRef = mySystem.actorOf(Props(new Hello("hi")), name = "greeter") // Hello 타입의 액터 생성

  // greeter와 교신. 'hi'라는 메세지를 보낸다 // Received a hi!!! 출력
  hiActor!"hi"
  Thread.sleep(1000)

  // greeter와 교신. 3이라는 메세지를 보낸다. // Unexpected message '3' 출력
  hiActor!3
  Thread.sleep(1000)

  // 액터 시스템 종료
  mySystem.terminate()

}


/*
  * Call-by-value / -name
  - Call-by-value : 매개변수에 대한 계산을 끝낸 후 함수로 진입 <--- 스칼라에서 더 많이 쓰이는 타입
  - Call-by-name : 함수 내에서 매개변수가 쓰일 때 계산 시작 <--- 함수가 일급객체로서 매개변수로 넘어갈 때 사용
                   함수를 선언할 때 매개변수 타입을 =>로 하면 Call by Name을 하도록 할 수 있음
 */

