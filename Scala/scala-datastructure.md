
## 스칼라의 자료구조 - 컨테이너

#### 배열
- 모든 원소가 같은 타입으로 이루어진 변경 '가능한' 시퀀스 객체 <--- 사이즈가 아니라 값을 변경할 수 있다는 의미임!
- 스칼라 배열도 여타 객체와 마찬가지로 평범한 클래스의 인스턴스
- 배열 원소에 접근할 때 []가 아니라 ()를 이용
- 배열을 val로 지정한 경우 변수를 재할당할 수는 없지만, 변수가 나타내는 객체는 변경 '가능'
- 변수 뒤에 하나 이상의 값을 괄호로 둘러싸서 호출하면 스칼라는 그 코드를 변수에 대해 apply라는 메소드를 호출하는 것으로 바뀐다.
  즉, greet(i)는 greet.apply(i)로 바뀐다.
- Array(1, 2, 3) 이런식으로 만들 수 있다.
- 배열의 내용을 출력하려면 .mkString(",") 와 같은 메소드를 이용해야 한다.


#### 리스트
- 모든 원소가 같은 타입으로 이루어진 변경 '불가능한' 시퀀스 객체
- 리스트의 내용을 변경하는 것 같아 보이는 메소드를 호출하면 리스트 자체를 변경하지 않고 새 값을 갖는 리스트를 새로 만들어 반환
- :: (cons) 는 두 리스트를 이어붙이는 연산자(스칼라에선 메소드) // cons와 빈 리스트는 Nil이라고 줄여 쓸 수 있음


#### 튜플
* 각기 다른 타입의 원소로 이루어진, 변경 불가능한 컨테이너 객체
* 인덱스가 0이 아니라 1부터 시작
* 튜플은 리스트와 다르게 각 원소들의 타입이 다르기 때문에 tuplename(2) 이런식으로 접근할 수 없음
* 튜플은 여러 타입의 객체를 담을 수 있는 자료형으로, 원소들을 소괄호로 묶어서 표현한다.
* 각 원소들의 자료형은 일치해야 할 필요가 없다.
* 스칼라에서 튜플은 내부적으로 담고 있는 객체의 수에 따라 다른 클래스로 구현
    * 예를 들어 3개의 객체를 담고 있으면 Tuple3 클래스를 이용해야 하며, (ex. val t1 = new Tuple3(1, "hello", true)) Tuple1부터 Tuble22까지 사용할 수 있고 그 이상을 쓰려면 컬렉션과 같은 다른 자료구조를 사용해야 한다. 튜플의 값에 접근하려면 ~._1, ~._2 와 같은 메소드를 이용한다.
* 튜플을 이용하면 한 번에 여러 개의 값을 리턴할 수 있다.

