package Akka.Hama

import akka.actor.{Actor, Props}
import akka.event.Logging

class HierachyActor extends Actor {

  val log = Logging(context.system, this)

  val receive = {
    case "create" =>
//      context.actorOf(Props[ChildActor])
      log.info(s"created a new child - children = ${context.children}")
    case "hi" =>
      log.info("Kids, say hi!")
      for (c <- context.children) c ! "hi"
    case "stop" =>
      log.info("parent shopping")
      context.stop(self)
  }

}
