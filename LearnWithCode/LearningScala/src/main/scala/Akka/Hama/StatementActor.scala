package HamaCode

import HamaCode.ActorsHierarchy.ourSystem
import akka.actor.{Actor, Props}
import akka.event.Logging

// import scala.collection.concurrent.Debug.log
import scala.collection.mutable
import scala.io.Source

/*
  상태머신 (상태에 따른 행동변화)
  - 액터가 자신의 상태를 바꿈에 따라 메시지를 처리하는 방식도 달라진다.
 */

// receive 메소드 안에 상태에 따른 여러 행위를 넣어 처리하는, 바람직하지 않은 방법
class BadCountdownActor extends Actor {
  var n = 10

  // if문을 통해 부분함수가 다르게 반환되는데, 이렇게 하지 말 것
  def receive =
    if (n > 0) {
    case "count" =>
      // ...do something ..
      n -= 1
    } else PartialFunction.empty

}

// become을 사용해 상태에 따른 행위를 애초에 나누어서 처리하는 방법
class GoodCountdownActor extends Actor {
  var n = 10
  // 초기 receive 메소드에 counting이라는 메소드 지정. 초기 상태에 매핑되는 메소드가 counting
  def counting: Actor.Receive = {
    case "count" =>
      n -= 1
//      log(s"n = $n")
      // 액터의 상태가 n=0으로 바뀌면 context.become(done)을 이용해서 매핑되는 메소드도 done으로 바꿈
      if(n == 0) context.become(done)
  }
  def done = PartialFunction.empty
  def receive = counting
}



/*  --------------------------------    예         제    -------------------------------- */

class DictionaryActor extends Actor {
  private val log = Logging(context.system, this)
  private val dictionary = mutable.Set[String]()

  def receive = uninitialized
  def uninitialized: PartialFunction[Any, Unit] = {
    case DictionaryActor.Init(path) =>
      val stream = getClass.getResourceAsStream(path)
      val words = Source.fromInputStream(stream)
      for (w <- words.getLines) dictionary += w
//      context.become(initialzed) <-- 왜 오류나는지 함 봐야할듯,,
  }

}

object DictionaryActor {
  case class Init(path: String)
  case class IsWord(w: String)
  case object End
}

object ActorsBecome extends App {
  val dict = ourSystem.actorOf(Props[DictionaryActor], "dictionary")

  // 1) 초기상태 uninitialized
  // 2) Init이 호출되어 초기화 완료되면 -> initialized로 상태 바꿈
  // 3) initialized 상태에서는 IsWord 메세지를 받아서 작업
  // 4) 작업 끝을 알리는 메세지 수신
  // 5) uninitialized로 행동 다시 바꿈
  dict ! DictionaryActor.IsWord("program")
  Thread.sleep(1000)
  dict ! DictionaryActor.Init("/org/learningconcurrency/words.txt")
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("program")
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("balaban")
  Thread.sleep(1000)
  dict ! DictionaryActor.End
  Thread.sleep(1000)
  dict ! DictionaryActor.IsWord("termination")
  Thread.sleep(1000)
  ourSystem.terminate()

}