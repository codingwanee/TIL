package cookbook

import akka.actor.Actor

class SummingActorWithContructor (initialSum: Int) extends Actor {

  var sum = 0
  override def receive: Receive = {
    case x: Int => sum = initialSum + sum + x
    //case
  }

}
