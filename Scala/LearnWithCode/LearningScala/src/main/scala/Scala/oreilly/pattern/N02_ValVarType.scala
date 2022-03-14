package Scala.oreilly.pattern

class N02_ValVarType {

  // 예시 1)
  for {
    x <- Seq(1, 2, 2.7, "one", "two", "three")    // Seq[Any] 타입의 목록을 사용해 여러 타입이 섞인 값을 처리
  } {
    val str = x match {                           // x는 Any 타입임
      case 1    => "int 1"                        // x가 1인 경우 일치
      case i: Int     => "other int: " + i        // 다른 Int 값과 일치. 안전하게 x의 값을 Int로 변환하고, i에 대입한다.
      case d: Double  => "a double: "  + x        // 모든 Double 값과 일치. 이 경우 x의 값을 Double 뵨수 d에 대입한다.
      case "one"      => "string one"             // 문자열 "one"과 일치
      case s: String  => "other string: " + s     // 다른 문자열과 일치. 이 경우 x의 값을 String 변수 s에 대입한다.
      case unexpected => "unexpected value: " + unexpected  // 타입을 표기하지 않았기 때문에 Any 타입으로 추론. 모든 다른 입력값과 일치한다. 이 절이 '기본'절 역할을 한다.
    }
    println(str)  // 돌려받은 문자열 출력
  }

  // 예시 2) 위치지정자 사용
  for {
    x <- Seq(1, 2, 2.7, "one", "two", "three")
  } {
    val str = x match {
      case 1          => "int 1"
      case _: Int     => "other int: " + x        // 위치지정자 (_)로 변수 대체
      case _: Double  => "a double: "   + x       // 위치지정자 (_)로 변수 대체
      case "one"      => "string one"
      case _: String  => "other string: " + x     // 위치지정자 (_)로 변수 대체
      case _          => "unexpeted value: " + x  // 위치지정자 (_)로 변수 대체

      // 문자열을 만들 것이기 때문에 실제로는 각각의 타입에 따른 실제값을 만들 필요가 없다.
      // 따라서 모든 경우에 x를 사용할 수 있다.
    }
    println(str)
  }
}
