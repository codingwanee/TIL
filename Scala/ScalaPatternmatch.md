# 스칼라의 패턴 매칭 Pattern Matching

* 스칼라의 패턴 매칭은 처음 보면 C 언어의 case문과 비슷하다.
* 전형적인 C 언어의 case문에서는 서수 타입(ordinal)의 값에 대해서만 매치시킬 수 있고, 간단한 식에 대해서만 매치가 가능하다.
* 스칼라의 패턴 매칭을 사용하면 타입, 와일드카드, 시퀀스, 정규 표현식에 대한 판별은 물론 객체 상태를 깊이 관찰하는 것도 가능하다.
* 위치 지정자를 이용해 더 편리하게 이용 가능


#### 스칼라 패턴매칭의 특징
* 셀렉터 match { case }로 표현되는 표현식이다.
* 스칼라의 case는 다음 case로 fall through 하지 않는다.
* 매치에 성공하지 않는 경우 MatchError 예외가 발생한다.

#### 단순 매치

```Scala
  val bools = Seq(true, false)

  for (bool <- bools) {
    bool match {
      case true => println("Got heads")
//      case false => println("Got tails")  // 이 문장을 주석으로 가리면 오류가 난다.
      // 컴파일러가 boolean 타입엔 true와 false라는 두 가지 경우가 있는 것을 아는데,
      // 둘 중 하나만 있으면 매치가 완전하지 않다는 것을 알기 때문
    }
  }
```
* 위의 예시 코드처럼 컴파일러가 완전하지 않은 매치를 발견하는 경우 경고가 표시된다.
* 이러한 경우에는 if를 통해 간단히 표현하는 게 더 나음.


#### 매치 내의 값, 변수, 타입


* 자바와 마찬가지로 match절은 앞에서부터 차례대로 처리된다. 따라서 더 구체적인 절일수록 앞으로 나와야 한다. 그렇지 않으면 더 구체적인 절은 결코 매치를 시도할 대상이 될 수 없다.
* 따라서 '기본절'(unexpected가 있는 부분)은 가장 마지막에 와야 한다.



#### 시퀀스에 일치시키기

* Seq는 정해진 순서대로 원소를 순회할 수 있는 List나 Vextor 등의 모든 구체적인 컬렉션 타입의 부모 타입이다.

* 패턴 매칭과 재귀를 사용해서 Seq를 순회하는 전통적인 관용구를 살펴보면서, 시퀀스에 대해 몇가지 유용한 기본 사항을 배우자.



#### 튜플과 매치

```Scala
val langs = Seq(
  ("Scala", "Martin"),
  ("Clojure", "Rich", "Hickey"),
  ("Lisp", "John", "McCartney")
)

for (tuple <- langs) {
  tuple match {
    case ("Scala", _, _) =>	// 첫 번째 원소가 "Scala" 문자열인 3차원 튜플과 일치(두 번째와 세 번째 원소는 무시)
      println("Found Scala")	
    case (lang, first, last) =>	// 원소의 타입과 관계없이 3원소 튜플과 일치시킨다.
      println(s"Found other language: $lang ($first, $last)") // 컴파일러가 타입을 자동으로 추출
	}
}
```



#### 케이스절과 매치

```Scala
for ( i <- Seq(1,2,3,4)) {
  i match {
    case _ if i%2 == 0 => println(s"even: $i")	// i가 짝수인 경우에만 일치
    case _						 => println(s"odd: $i")		// 위에서 벗어난 케이스(홀수)에 일치
  }
}
```



#### 케이스 클래스와 매치

```scala
case class Address(street: String, city: String)
case class Person(name: String, age: Int, address: Address)

val alice		= Person("Alice",		25,	Address(_, "Chicago"))	=> println("Hi Alice!")
val bob			= Person("Bob",			29, Address(""))
```


