# ExecutionContext
- Scala Standard Library에서 제공하는 scala.concurrent.ExecutionContext 번역문
- 원문: https://www.scala-lang.org/api/2.13.3/scala/concurrent/ExecutionContext.html



## ExecutionContext
- Companion object ExecutionContext
- `trait ExecutionContext extends AnyRef`

- ExecutionContext는 프로그램 로직을 비동기적으로 실행할 수 있으며, 스레드풀에 일반적이지만 필수적이진 않다.

- 일반적으로 ExecutionContext의 목적은 실행 메소드로 전달되는 Runnable을 비동기적으로 실행하는 것이다. ExecutionContext의 특별한 목적으로는, ExecutionContext를 이용하여 실행되어 동기적이되 명시적으로 안전한 코드만 통과시키도록 하는 것이다.

- Future.onComplete와 같은 API들은 callback 함수와 암시적 ExecutionContext의 제공을 요구한다. 암시적 ExecutionContext는 콜백을 실행하는 데 이용된다.

- 단순히 `scala.concurrent.ExecutionContext.Implicits.global`를 import해서 암시적 ExecutionContext을 얻을 수도 있지만, 어플리케이션 개발자들은 신중하게 실행 정책(execution policy)을 설정해야 한다; 궁극적으로, 하나의 어플리케이션에 한 부분- 또는 코드에서 논리적으로 연관된 섹션별로-에 부여되어야 어떤 ExecutionContext이 이용될 지 결정할 수 있다. 즉, 당신은 하드코딩, 특히 `scala.concurrent.ExecutionContext.Implicits.global`을 임포트 하는 등의 일을 피하고 싶을 것이다. 권장되는 방법으로는 (implicit ec: ExecutionContext)를 ExecutionContext가 필요한 메소드 또는 클러스 생성자 파라미터에 추가하는 방법이다.

- 그런 다음, 전체 애플리케이션 또는 모듈에 대해 한 곳에서 특정 ExecutionContext을 로컬로 가져와 개별 메소드에 암시적으로 전달한다. 또는 요구되는 ExecutionContext에 대해 로컬에 암시적 변수(implicit val)을 정의할 수도 있다.

- 커스텀 ExecutionContext은 IO 블록 또는 긴 시간 수행되는 계산을 실행하기에 적절하다. ExecutionContext.fromExecutorService 그리고 ExecutionContext.fromExecutor는 커스텀 ExecutionContext을 생성하기에 좋은 방법이다.

- ExecutionContext의 목표는 실행 코드를 사전적으로 범위짓기 위한 것이다. 즉, 각각의 메소드, 클래스, 파일, 패키지, 또는 어플리케이션이 어떻게 자신의 코드를 실행시킬지 정의하게 하는 것이다. 이렇게 함으로써 이슈들, 예를 들면 실행중인 애플리케이션이 네트워킹 라이브러리에 속한 스레드풀에 대해 콜백하는 상황 등을 방지할 수 있다. 네트워킹 라이브러리가 가진 스레드풀의 사이즈는 안전하게 구성될 수 있으며, 단지 라이브러리의 네트워크 기능들이 영향받을 수 있다는 것만 알면 된다. 어플리케이션이 콜백 실행은 독립적으로 구성될 수 있다.


## 어노테이션 Annotations

#### @implicitNotFound

```scala
(msg = """Cannot find an implicit ExecutionContext. You might pass
an (implicit ec: ExecutionContext) parameter to your method.

The ExecutionContext is used to configure how and on which
thread pools Futures will run, so the specific ExecutionContext
that is selected is important.

If your application does not define an ExecutionContext elsewhere,
consider using Scala's global ExecutionContext by defining
the following:

implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global""")
```

- 메시지 번역:
    - 암시적 ExecutionContext를 찾을수가 없습니다. 메소드 인자로 (implicit ec: ExecutionContext) 를 넘겨야 합니다.
    - ExecutionContext는 Future가 어떻게, 그리고 어떤 스레드풀을 실행할 것인지 구성하고, 따라서 특정한 ExecutionContext이 선택되는 것이 중요합니다.
    - 만약 당신의 애플리케이션이 ExecutionContext를 아무데서도 정의하고 있지 않다면, 스칼라의 global ExecutionContext를 다음과 같이 정의하는 것을 고려해보십시오:
        implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global


## 계층구조 Type Hierarchy

#### 상위유형 Linear Supertypes
- AnyRef
- Any

#### 서브클래스 Known Subclasses
- parasitic
- ExecutionContextExecutor
- ExecutionContextExecutorService
