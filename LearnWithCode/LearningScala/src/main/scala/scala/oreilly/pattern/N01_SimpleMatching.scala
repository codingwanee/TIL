package scala.oreilly.pattern

class N01_SimpleMatching {

  val bools = Seq(true, false)

  for (bool <- bools) {
    bool match {
      case true => println("Got heads")
      case false => println("Got tails")  // 이 문장을 주석으로 가리면 오류가 난다.
      // 컴파일러가 boolean 타입엔 true와 false라는 두 가지 경우가 있는 것을 아는데,
      // 둘 중 하나만 있으면 매치가 완전하지 않다는 것을 알기 때문
    }
  }

}
