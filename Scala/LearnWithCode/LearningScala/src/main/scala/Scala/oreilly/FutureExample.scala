package Scala.oreilly

import scala.concurrent.Future

// Future API는 동시 연산을 어떻게 실행할지 ExecutionContext를 지정해서 설정할 수 있다.
// 이 import는 자바의 ForkJoinPool 기능을 사용해서 자바 스레드를 관리하는 기본 ExecutionCeontext를 가져온다.
import scala.concurrent.ExecutionContext.Implicits.global

// 다섯 가지 작업을 동시에 시작하고, 완료 시 결과를 처리하는 예제
class FutureExample {

  def sleep(millis: Long) = {    Thread.sleep(millis)
  }

  // 쓸모는 없는데 바쁜 일
  def doWork(index: Int) = {
    // 0부터 1000 사이 임의의 시간을 sleep에 넘겨서 바쁜 작업수행을 흉내냄
    sleep((math.random * 1000).toLong)
    index
  }

  // 1 이상 5 이하 정수범위의 원소를 foreach로 순회하면서
  // scala.concurrent.Future.apply를 호출(Future 싱글턴 객체에 있는 팩토리 메서드)
  // 여기서는 Future.apply에 작업할 내용을 표현하는 익명함수를 전달
  (1 to 5) foreach { index =>
      val future = Future { // Future.apply 메소드 호출
        doWork(index)
      }

      // Future가 성공적으로 완료하는 경우에 호출될 콜백
      Future onSuccess {
        case answer: Int => println(s"Success! returned: $answer")
      }

      // Future의 실패를 처리할 콜백을 등록
      // 실패는 Throwable에 캡슐화 됨
      Future onFailure {
        case th: Throwable => println(s"FAILURE! returned: $th")
      }
  }


  // 종료 전 sleep을 호출해서 모든 작업이 끝날 때까지 기다림
  sleep(1000)
  println("Finished!")
}
