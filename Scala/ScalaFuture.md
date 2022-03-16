# 스칼라의 Future

* scala.concurrent.Future API는 암시적 인자를 활용해서 코드에 필요한 준비 코드를 줄여주는 동시성 도구다.

* 수행할 작업 중 일부를 Future로 감싸면 그 작업을 비동기적으로 수행한다. 그리고 Future API는 결과가 준비된 경우 콜백을 호출해주는 등 결과를 처리할 수 있는 다양한 방법을 제공한다.



#### ExecutionContext

import scala.concurrent.ExecutionContext.Implicits.gloal

* connection pool과 같은 개념
* 퓨쳐가 일반적으로 사용하는 ExecutionContext이며, implicit 키워드로 선언된다. 따라서 

### 퓨처 Future

Future는 '미래의 값을 저장하고 있는 객체'라고 이해하면 된다.

* 스칼라의 Future는 
* 스칼라와 자바의 Future의 가장 큰 차이점은, 두 퓨쳐 모두 비동기적인 계산의 결과를 표현하지만, 자바의 퓨처에서는 블로킹(blocking) 방식의 get으로 값을 가져와야 한다는 점이 다르다.
* 스칼라의 Future는 결합법칙을 만족시키 못해 Monad가 아닌 Monadic Type이다.



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



현상과 서사

### 옵션 Option

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
