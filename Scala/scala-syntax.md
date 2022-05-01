
# 스칼라 for ... yield 구문
* 스칼라의 for .. yield 는 for comprehension을 좀 더 가독성 있게 만든 문법설탕이다.
* 참고한 글 https://knight76.tistory.com/entry/scala-for-문-yield

#### 결과를 vector로 반환
```scala
val a =  for(i <- 1 to 3) yield i;
// 결과 Vector(1, 2, 3)
```

#### 결과값에 연산
```scala
val a =  for(i <- 1 to 3) yield i * 100;
// 결과 Vector(100, 200, 300)
```

#### 결과값을 조건문으로 비교
```scala
val a =  for(i <- 1 to 3) yield i > 2;
// 결과 Vector(false, false, true)
```

#### 결과값을 조건문으로 filtering
```scala
val a = for( i <- 1 to 3 ; if i == 1 ) yield i
// 결과 Vector(1)
```

* if문을 여러 개 사용할 수도 있다.
```scala
val a = for( i <- 1 to 3 ; if i == 1 ; if i == 2 ) yield i
```

#### 좀 더 확장된 사용법
```scala
val list = List(1, 2, 3)
val a = for {
  element <- list
} yield {
  element
}

// 결과 List(1, 2, 3)
```


#### 스칼라의 list - comprehensions
* 일반적인 언어의 list - comprehensions
    [() for () in ()]
    [( A ) for ( B ) in ( C )]

    A : 변수를 활용한 값    // 실제로 할당될 값
    B : 사용할 변수 이름    // 블락 안에서 사용될 인자의 이름
    C : 순회할 수 있는 값   // range처럼 하나씩 값을 살펴볼 수 있는 것(iterable)

* 스칼라의 for - comprehensions / yeild --- 문법설탕
    * 스칼라의 yield는 for comprehensions의 일부분으로 사용되며, 다른 언어의 list-comprehension의 일반화
    * return 키워드는 모든 결과값을 메모리에 올려놓아야 하는 반면, yield 키워드를 사용할 때는 결과값을 하나씩 메모리에 올려놓음

