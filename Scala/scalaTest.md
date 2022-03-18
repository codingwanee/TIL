## 스칼라의 단언문과 테스트

* 참고글. https://hongku.tistory.com/368

  

#### 단언문

* 일반적으로 assert(조건문)는 어떤 변수에 원하는 값이 들어있는지 확인하는 메소드로, 스칼라에서는 조건을 만족하지 못하면 AssertionError를 발생시킨다.
* 인자 2개를 받는 assert(조건, 설명)도 있는데, 이 때 설명은 Any타입을 가진다.



#### 술어

* w <= _.width: 술어
* _(밑줄)은 술어가 갖는 유일한 인자. widen 메소드의 결과
* ensuring은 술어에게 메소드 결과를 넘긴다.



#### 명세절로 테스트하기 (specifier clause)

```scala
"테스트 할 주제에 이름붙이기" should //주제 다음에 should, must, can
	"작동 설명" in {
    // .. 테스트 할 코드들 ..
  }
```

* 각종 Matchers 트레이트와 믹스인하면 더욱 가독성 좋은 명세절을 작성할 수 있다.



#### 원하는 양식대로 실패 보고 받기

* DiagrammedAssertions : 더욱 상세한 정보를 다이어그램 형식으로 보고싶을 때
* assertResult : 실제가 기대치와 다르다는 사실을 강조하고 싶을 때
* assertThrows : 예외를 검사하고 싶을 때 (원하는 예외가 발생하지 않으면 TestFailedException 발생)
* intercept : assertThrows와 동작은 같지만, 원하는 예외가 발생했을 경우에만 예외 반환



#### 프로퍼티 기반 테스트

* extends WordSpec, with PropertyChecks로 각 프로퍼티에 대해 테스트해볼 수 있다.
* 스칼라체크가 시도하는 모든 값을 프로퍼티가 만족해야만 테스트 통과.
* 만족하지 않으면 TestFailedException을 즉시 던지고 테스트 종료



#### 테스트 조직과 실행

* 스칼라스위트 안에 스위트를 포함시킴으로써 큰 테스트를 조직화한다.
* 어떤 Suite가 실행되면, 그 안에 테스트, 내부에 있는 Suite의 테스트도 실행된다. 즉, 트리구조의 루트 Suite 객체를 실행하면 트리 전체의 Suite를 실행하게 된다.
* 수동 처리 : nestedSuites 메소드를 오버라이드하거나, 포함시키고 싶은 스위트를 Suite 클래스의 생성자에 전달
* 자동 처리 : 스칼라테스트의 Runner에 패키지 이름을 전달하면 된다.
