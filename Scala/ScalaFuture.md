# Future

## 퓨처 Future
* JVM 언어들의 동시성 도구, Future
* 비동기적 프로그래밍을 위한 라이브러리
* Future는 '미래의 값을 저장하고 있는 객체'라고 이해하면 된다.

#### 자바와 스칼라의 Future 비교
* 스칼라와 자바의 Future의 가장 큰 차이점은, 두 퓨쳐 모두 비동기적인 계산의 결과를 표현하지만, 자바의 퓨처에서는 블로킹(blocking) 방식의 get으로 값을 가져와야 한다는 점이 다르다.
* 스칼라의 Future는 결합법칙을 만족시키 못해 Monad가 아닌 Monadic Type이다.


## 자바의 Future
* 비동기적인 연산의 결과를 표현하는 클래스
* 멀티스레드 환경에서 처리된 어떤 데이터를 다른 쓰레드에 전달
* 내부적으로 Thread-safe하도록 구현되었기 때문에 synchronized block을 사용하지 않아도 됨
* 블로킹 방식의 get()으로 결과를 얻어와야 함 -> Future가 완료될 때까지 다른 계산을 수행 X


## 스칼라의 Future
* 비동기적 프로그래밍을 지원하기 위해 Scala가 제공하는 표준 라이브러리.
* Future를 사용해 변경 불가능한 상태를 비동기적으로 변환하여 블로킹 방식으로 인한 동시성 처리의 어려움을 피할 수 있게 해줌.
* 스칼라의 Future는 계산결과의 완료여부와 관계없이 결과값의 변환을 지정할 수 있다.
* 각 변환은 원래의 Future를 지정한 함수에 따라 변환한 결과를 비동기적으로 담은 것을 표현하는 새로운 Future를 생성
* 계산을 수행하는 스레드(thread)는 암시적으로 제공되는 실행 컨텍스트(execution context)를 사용해 결정
* 불변값에 대한 일련의 변환으로 비동기 계산을 표현할 수 있고, 공유 메모리나 락에 대해서 신경쓸 필요가 없음
* 퓨쳐'값' =/ 퓨쳐'계산'
* scala.concurrent.Future API는 암시적 인자를 활용해서 코드에 필요한 준비 코드를 줄여주는 동시성 도구
* 수행할 작업 중 일부를 Future로 감싸면 그 작업을 비동기적으로 수행한다. 그리고 Future API는 결과가 준비된 경우 콜백을 호출해주는 등 결과를 처리할 수 있는 다양한 방법을 제공


#### trait Future[T]
* 퓨쳐값.
* T 타입의 실제 우리가 리턴받기 원하는 '객체'를 포함

#### def apply[T](b: => T) (implicit e: ExcecutionContext) : Future[T]
* 퓨처계산.
* 실제 계산을 수행하는 '함수'를 매개변수로 넣어주고 있음.
* 암시적으로 ExecutionContext가 매개변수로 들어간다. 즉 쓰레드풀을 넣어주는 것

- 참고한 글: https://sungjk.github.io/2017/08/09/scala-future.html



#### ExecutionContext

```scala
import scala.concurrent.ExecutionContext.Implicits.gloal
```

* connection pool과 같은 개념
* 퓨쳐가 일반적으로 사용하는 ExecutionContext이며, implicit 키워드로 선언


### 프로미스 Promise

* 비동기 코드를 안전하고 읽기 쉽게 작성하기 위한 스칼라의 장치.
* Promise는 코드가 성공적으로 실행되었을 때의 값을 가지고 있거나, 코드가 실패했을 때 실패한 이유를 가지고 있다.

* Promise는 아직 완료되었는지 알 수 없는 일을 한 번 감싼 타입이다.
* Future와 하는 일은 같지만, Future는 이미 생성된 모나드를 완료시키지는 못하는 read-only Promise이다.
* Future와 달리 Promise는 생성 당시 빈 컨테이너(Empty container)이지만 값이 계산되면 채워지므로 아래와 같이 작성 가능하다.

```scala
// 외부 시스템의 API를 사용하는 경우, Promise를 이용해 해당 콜백의 결과값을 가져올 수 있다.

def createCustomToken[T](userId: UserId)(handler: String => T): Future[T] = {
	val promise = Promise[T]

	Firebase.auth.createCustomToken(userId.toString)
		.addOnSuccessListener(new OnSuccessListener[String]() {
			override def onSuccess(customToken: String): Unit =
				promise.success(handler(customToken))
		})
		.addOnFailureListener(new OnFailureListener {
			override def onFailure(e: Exception): Unit =
				promise.failure(e)
		})

	promise.future
}
```


#### 옵션 Option

* Option의 조건 두 가지
   	1) null을 안전하게 대체하기 위한 것
   	2) 연속체인에서 안정적으로 사용하기 위한 것



##### Option에서 값 가져오기 (get과 isDefined)

```Scala
val name : Option[Int] = names.get("Wanee")
```


##### getOrElse로 사용하기



##### 패턴매칭으로 사용하기



##### Option 타입의 파라미터 사용



### 이더 Either

* 둘 중에 하나의 값을 가질 수 있는 타입이다.



##### Either에 값 담기 - Right, Left

* 인자로 원하는 값이 들어오면 Right 즉 Either의 오른쪽에 담고, 반대는 Left 즉 왼쪽에 담는다.

  ```Scala
  def eitherSample(num: Option[Int]): Either[String, Int] = num match {
    case Some(n) => Right(n)	// 원하는 대로 int값이 들어오면 Right에 저장
    case None => Left("Error! Number is missing!")	// 원하는 값이 없으므로 String을 Left에 저장
  }
  
  eitherSample(Some(7))
  ```


##### Either 값 사용하기 - isRight, isLeft

 * isRight, isLeft 메소드는 boolean 을 반환한다.
 * 이 두 메소드를 사용해 어느쪽에 제대로 된 값이 들어갔는지 확인할 수 있다.

```Scala
val res = eitherSample(Some(7))

if (res.isRight)
	println("Number is %s".format(res.right.get))
else if (res.isLeft)
	println("Error message => %s".format(res.left.get))
```


##### 패턴 매칭으로 사용하기

```Scala
val result = eitherSample(Some(7)) match {
  case Right(num) => num
  case Left(err)	=> err
}
```


### 트라이 Try

* 예외 처리를 위해 스칼라가 제공하는 타입니다.
* Try[T]는 Either와 유사하게 Success[T]와 Failure[T]를 가질 수 있는데, 언뜻 굉장히 비슷하지만 Try의 Failure는 오직 Throwable 타입만 될 수 있다는 점이 다르다.
