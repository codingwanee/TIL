package cookbook

import akka.actor.Props
import akka.actor.ActorSystem

class HelloAkkaActorSystem extends App {
  val actorSystem = ActorSystem("HelloAkka")
  println(actorSystem)

  val actor = actorSystem.actorOf(Props[SummingActor], "summingator")
}
