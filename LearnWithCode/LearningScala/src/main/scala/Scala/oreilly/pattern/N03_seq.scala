package Scala.oreilly.pattern

class N03_seq {

  val nonEmptySeq       = Seq(1, 2, 3, 4, 5)
  val emptySeq          = Seq.empty[Int]
  val nonEmptyList      = List(1, 2, 3, 4, 5)
  val emptyList         = Nil   // 빈 List를 표현하는 특별 객체
  val nonEmptyVector    = Vector(1, 2, 3, 4, 5)
  val emptyVector       = Vector.empty[Int]
  val nonEmptyMap       = Map("one" -> 1, "two" -> 2, "three" -> 3)
  val emptyMap          = Map.empty[String, Int]

  def seqToString[T] (seq: Seq[T]): String = seq match {    // T라는 타입에 대해 Seq[T]로부터 String을 생성하는 재귀적 메서드를 정의한다.
    case head +: tail => s"$head +: " + seqToString(tail)   // 모든 비어있지 않은 Seq와 일치
    case Nil => "Nil"                                       // 모든 비어있는 Seq와 일치. Nil(빈 List를 표현하는 특별 객체)을 사용하여 모든 빈 리스트와 일치시킬 수 있다.
  }

  for (seq <- Seq(
      nonEmptySeq, emptySeq, nonEmptyList, emptyList,
      nonEmptyVector, emptyVector, nonEmptyMap.toSeq, emptyMap.toSeq)) {
    println(seqToString(seq))
  }

}
