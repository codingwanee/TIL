package cookbook

import akka.actor.Actor

class SummingActor extends Actor {
  // 액터 내부 상태
  var sum = 0

  override def receive: Receive = {
    // Integer 메시지를 수신함
    case x: Int => sum = sum + x
    println(s"my state as sum is $sum")

    // 기본 메시지를 수신함
    case _ => println("I don't know what are you talking about")
  }

}
