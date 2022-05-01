package HamaCode

import HamaCode.ActorsHierarchy.ourSystem
import akka.actor.Status.{Failure, Success}
import akka.actor.{Actor, Props, Status, Terminated}
import akka.event.Logging
import akka.pattern.gracefulStop

import scala.concurrent.duration.DurationInt

/*
  액터를 멈추는 방법 4가지
  0. context.stop(self) : 멈추게 하자. (우편함의 메시지 날라감)
  1. actor ! Kill : 멈추진 말고 단순 재시작하게 하자. (우편함의 메세지 살아있음)
  2. actor ! PoisonPill : 어떤 메세지까지는 처리하고 중단하게 하자. (PoisonPill 메세지 전까지 모두 처리)
  3. watch : 다른 액터가 끝날때까지 기다리게 하자. (액터 내부에서 기다림)
  4. gracefulStop : 다른 액터가 끝날때까지 기다리게 하자.(액터 외부에서 기다림)

 */

object GracefulPingy {
  object CustomShutdown
}

/*
  3. watch : 다른 액터가 끝날때까지 기다리게 하자. (액터 내부에서 기다림)
    - pongy 액터를 watch를 통해 감시
    - pongy 가 postStop 메소드를 수행 --> GracefulPingy는 Terminated(`pongy`) 메세지 받고난 후 자신을 종료
 */
class GracefulPingy extends Actor {

  var log = Logging(context.system, this)

  // watch 로 pongy 액터를 감시
  val pongy = context.actorOf(Props[Pongy], "pongy")
  context.watch(pongy)

  def receive = {
    case GracefulPingy.CustomShutdown =>
      context.stop(pongy)
    case Terminated(`pongy`) =>
      context.stop(self)
  }

}

/*
  4. gracefulStop : 다른 액터가 끝날때까지 기다리게 하자.(액터 외부에서 기다림)
    - 보통 애플리케이션의 주 쓰레드에서 사용
    - gracefulStop에 인자로 액터, 타임아웃, 종료메시지 넣는다.
    - 퓨처를 통해 시간 안에 종료되면 Success(x)가 호출 / 아니면 Failure(t)가 호출
 */

object CommunicatingGracefulStop extends App {
  val grace = ourSystem.actorOf(Props[GracefulPingy], "grace")
  // stopped는 퓨처
  // gracefulStop의 인자로 grace라는 액터, 타임아웃, 종료메시지 넘김
  val stopped = gracefulStop(grace, 3.seconds, GracefulPingy.CustomShutdown)
/*
  stopped onComplete {

    case Success(sc) =>
      log("graceful shutdown successful")
      ourSystem.terminate()
    case Failure(t) =>
      log("grace not stopped!")
      ourSystem.terminate()

  }

 */
}
