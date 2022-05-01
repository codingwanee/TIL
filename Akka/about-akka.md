# Akka

# I. 시작하기

## 1.1. 아카란?

### 1.1.1 새로운 프로그래밍 모델이 필요한 이유

#### 캡슐화의 문제

단일 쓰레드 상황에서만 캡슐화 보장. 다중 쓰레드 상황에선 거의 항상 내부 상태 손상 야기.

메서드에 락lock을 추가하면 다중 쓰레드에서도 캡슐화를 유지할 수 있다고 생각할 수 있다. 하지만 현실적으로 비효율적이고 데드락 발생 가능성이 높다. 분산락 또한 많은 제약이 있음.

#### 현대 컴퓨터 아키텍처상에서의 공유 메모리에 대한 착각

진정한 의미의 공유 메모리는 존재하지 않는다. CPU는 메모리에 직접 값을 쓰는 게 아니라 로컬 영역인 cache lines에 쓰는데, 이는 다른 코어에서 볼 수 없다. JVM의 경우 `volatile` 로 표시하여 공유 메모리를 명시하는데, 캐시간 데이터를 옮기는 작업은 비싼 작업이라 캐시라인간 병목이 생길 수 있다.

동시성의 엔티티들 간에 명시적으로 메시지를 통해 데이터나 이벤트를 전파하는 방법이 근본적인 해결 방법.

#### 콜 스택에 대한 착각

콜 스택 = 스택

> 현재 실행 중인 서브루틴에 관한 정보를 저장하는 스택 자료구조.
>
> 서브루틴의 실행이 끝났을 때, 제어를 반환할 지점을 보관

콜스택의 문제는 쓰레드가 백그라운드로 태스크를 위임할 때 생긴다. 호출자 caller, 워커 worker.

워커의 태스크 완료 또는 실패 여부를 호출자가 알기 어려움. 워커는 문제를 제대로 처리할 수 없어 호출자는 실패를 제대로 알 수 없다. 이는 메시지 및 이벤트 손실을 야기.

태스크 위임의 동시성 시스템은 실패를 처리해야 하며 실패에서 복구할 근본적인 방법 필요. 타임아웃과 같이 기한을 정할 수 있는 체계 필요.

### 1.1.2. 분산시스템과 액터 모델

액터를 사용한다는 것은

* 락에 의지하지 않고도 캡슐을 강화한다.
* 서로 시그널을 보내고 반응하는 협조적인 엔티티 모델을 사용하도록 진화시킨다.
* 현실 세계의 관점과 대응하는 실행구조 구현이 가능하다.

#### 락과 블로킹 없는 메시지 전송

액터는 메서드 호출이 아니라 메시지를 전송한다. 실행 쓰레드를 전달하지 않기 때문에 블로킹 없이 메시지를 계속 보낼 수 있어 같은 시간에 더 많은 일을 할 수 있다.

메시지를 수신하면 현재의 메시지를 처리한 후 실행 권한을 반환한다. (이는 액터와 객체의 공통점)

* 메시지 전송 (액터) :
  * 메시지에 반환값이 없음
  * 메시지를 보냄으로써 액터는 다른 액터에게 임무를 위임.
* 메서드 호출 (객체) :
  * 메시지에 반환값이 있어 기다려야 함
  * 블로킹될 수도 있고 다른 작업을 같은 쓰레드에서 실행해야만 한다.

액터는 메시지에 반응한다. 수신된 메시지를 순차적으로 한 번에 하나씩 처리하는 액터들이 동시적으로 동작한다. 하나의 액더 시스템이 동시에 많은 메시지를 처리하는 방법.

##### **액터가 필요로 하는 것**

* 메일박스 mailbox : 메시지가 쌓이는 큐
* 행위 behavior : 액터의 상태, 내부 변수
* 메시지
* 실행환경
* 주소

##### **액터 모델이 해결 가능한 문제**

* 쓰레드(실행)와 시그널이 분리됨으로써 캡슐화 유지
* 락이 필요 없어 쓰레드 경합 발생 방지
* 락이 필요 없어 블로킹되지 않음
* 액터의 상태는 로컬로 공유되지 않음. 메시지를 통해서만 변경되고 전파.

#### 액터의 오류 상황에 대한 처리

1. 위임한 태스크가 내부에서 발생한 에러로 인해 대상 액터에서 실패하는 경우

   캡슐화된 서비스는 손상되지 않으며 해당 태스크 자체만 실패. 평범한 메시지로 처리함.

2. 서비스 자체에 내부적인 에러가 발생하는 경우

   모든 액터는 트리 형태의 계층구조를 갖고 있기에, 액터가 실패하면 부모 액터에 통보되어 부모가 대응함.

   > 아카의 감독

##### 아카의 특징

###### 액터 Actor

* 분산, 동시성, 병렬화를 위한 간결하면서도 고수준의 추상화
* 비동기, 논블로킹, 고성능의 메시지 기반 프로그래밍 모델
* 경량의 이벤트 기반 프로세스

###### 내耐고장성 Fault Tolerance

* 수퍼바이저 계층 구조로 "let-it-crash" 사상 채용
* 진정한 내고장성 위해 액터를 다수의 JVM으로 확장 가능
* 스스로 치유, 중단 없는 내고장성 시스템 작성에 탁월

###### 위치 투명성 Location Transparency

###### 퍼시스턴스 Persistence



### 1.1.3. 아카 라이브러리의 모듈들

* 액터 라이브러리
* 리모팅
* 클러스터
* 클러스터 샤딩
* 클러스터 싱글톤
* 클러스터 퍼블리쉬-섭스크라이브
* 퍼시스턴스
* 분산 데이터
* 스트림
* http

#### 액터 라이브러리

아카의 핵심 라이브러리는 akka-actor이다. 객체와 달리 캡슐화의 대상은 객체의 상태state 뿐만 아니라 실행execution까지 포함한다.
액터의 통신은 메서드 호출이 아닌 메시지 전송을 통해 이루어진다.

#### 리모팅

리모팅은 다른 컴퓨터들에 존재하는 액터들이 서로 메시지를 교환할 수 있도록 해준다. 모듈처럼 하나의 JAR artifact로 배포된다.

#### 클러스터

액터들이 모여있는 액터 시스템들을 관리하기 위해 멤버쉽 프로토콜을 통하여 여러 액터 시스템들을 묶는다. 하나의 메타시스템을 조직한다.

#### 클러스터 샤딩

아카 클러스터를 구성하는 멤버들에게 액터를 배분하는 문제를 해결한다.

#### 클러스터 싱글톤

#### 클러스터 퍼블리쉬-섭스크라이브

클러스터 시스템을 운영할 때 전체 시스템이나 클러스터의 특정 시스템 그룹에 메시지를 배포

#### 퍼시스턴스

액터도 객체처럼 휘발성 메모리 상에 자신의 상태를 저장한다. 퍼시스턴트는 시스템이 재시작했을 때 액터가 자신의 현재 상태로 복구할 수 있도록 이벤트를 저장한다.

#### 분산데이터

#### 스트림

### 1.1.4. 스칼라

### 1.1.5. 스칼라와 자바 API

## 1.2 시작하기

# 기본개념

## 2.1. 용어와 개념

### 2.1.1. 동시성과 병렬성

* 동시성 : 여러 작업을 순차적으로 섞어서 실행 (타임 슬라이싱)
* 병렬성 : 완전히 같은 시간에 다중 작업 실행

### 2.1.2. 비동기와 동기

* 동기 : 객체의 메서드 호출. 호출자는 기다려야 함

* 비동기 : 호출자가 어떤 프로세스를 실행하고 호출한 후에 계속 진행

### 2.1.3. 블로킹 & 논블로킹

* 블로킹 : 한 쓰레드에서 지체가 발생. 다른 스레드를 무한으로 정체시킴. 리소스 독점

* 논블로킹 : 스레드가 다른 스레드를 무기한으로 지연시킬 수 없음

### 2.1.4. 데드락, 굶주림, 라이브락

* 데드락 : 복수의 쓰레드가 서로 상대방의 상태가 바뀌길 기다리는 상황
* 굶주림 : 진행되는 쓰레드가 있긴 하지만 하나 또는 그 이상의 쓰레드는 진행이 되지 않는 경우 (우선순위 높은 것만 처리하는 경우)
* 데드락 : 복수의 쓰레드가 서로 양보하면서 어느 누구도 리소스를 획득하지 못함

### 2.1.5. 경합 상태

* 외부의 비결정적 non-deterministic 영향에 의해서 일련의 이벤트의 순서가 어긋나는 상태

### 2.1.6. 비동기 보장

논블로킹의 속성

* Lock-freedom : 락을 사용하지 않았다. ▶︎ 데드락이 발생하지 않았다. 하지만 굶주림이 없다고 보장할 순 없다.
* Obstruction-feedom : 장애물이 없다. ▶︎ 락이 없는 객체는 장애물이 없다. 하지만 역은 참이 아님.
* OCG : Optimistic concurrency control : 장애물이 없다

## 2.2 액터 시스템

* 액터란 상태state와 행동방식behavior을 캡슐화한 객체
* 액터는 메일박스로 수신한 메시지를 통해서만 의사소통
* 액터시스템은 1~N 까지 쓰레드를 할당하는 무거운 구조체이므로 논리적 어플리케이션에서 하나만 생성하는 것이 좋다.

### 2.2.1. 계층적 구조

회사의 조직처럼 액터도 계층 구조를 형성한다. 특정 기능을 담당하는 하나의 액터는 더 작고 관리가능한 조각들로 나눠질 수 있다. 이를 위해 액터는 자신이 감독하는 자식액터를 만든다. 부모액터는 자식 액터의 유일한 수퍼바이저supervisor가 된다.

액터 시스템은 작업을 하나의 단위로 처리할 수 있을 만큼 작게 나누어 위임한다. 트리 형태로 구성된 계층에 따라 보다 적합한 사람과 발생한 문제에 대해 상의하여 해결책을 찾는 방식.

### 2.2.2. 환경설정 컨테이너

액터시스템은 스케줄링 서비스, 환경설정, 로깅과 같은 공유기능을 관리하는 단위.

복수의 액터 시스템들이 서로 다른 환경설정으로 하나의 JVM 내에 공존해도 아무 문제가 없다?

### 2.2.3. 액터의 모범사례

* 이벤트 중심 event-driven 방식으로 이벤트를 처리하고 응답을 만들어내야 한다.
* 액터는 변경가능한 상태가 되면 안된다. 불변 immutable 메시지를 사용한다.
* 최상위 액터는 최소한으로 만들어 진정한 계층적 hierarchical 시스템을 지향한다.

### 2.2.4. 블로킹 특별 관리

### 2.2.5. 직접 신경쓰지 않아도 되는 것들

메시지의 정확한 순서를 제어할 필요는 없다. 할 수도 없다. 걍 다 처리할 때 까지 기다리면 된다.

## 2.3. 액터란?

액터는 상태State, 행동방식Behavior, 메일박스Mailbox, 자식 액터Child Actors, 수퍼바이저 정책Supervisor Strategy이 담겨있는 컨테이너.

액터는 명시적인 생명주기를 가지며, 종료시 리소스를 어떻게 해제할지 제어해야 함.

### 2.3.1. 액터 레퍼런스

* 액터 레퍼런스는 액터를 바깥 세계로 표현하는 수단.

* 외부에서 액터의 내부를 들여다 보거나 액터의 상태를 잡아둘 수 없다.

### 2.3.2. 상태

- 상태기계, 카운터, 리스너, 완료되지 않은 요청 등의 변수
- 액터가 실패해서 수퍼바이저에 의해 재시작되면 액터가 최초 생성될 때처럼 상태를 처음부터 다시 구성한다. ▶︎ 시스템의 자가 치유 능력
- 퍼시스턴트를 사용해서 액터가 재시작하기 전에 수신했던 메시지를 재생replay해서 재시작 하기 직전의 상태로 자동 복구할 수 있다.

### 2.3.3. 행동방식

* 액터가 메시지를 받았을 때 어떤 행동(작업)을 할지 결정하는 함수
* 행동방식은 시시각각 변할 수 있으며 변경 방법은 두 가지가 있다. (ActorContext의 become & unbecome)
  * 행동방식의 로직에서 참조하는 상태 변수에 따라 코드를 작성
  * 실행 중에 함수 자체를 교체하는 방법

### 2.3.4. 메일박스

- 액터간 주고받는 메시지를 보관하는 곳
- 일반적으로 Queue 형태의 FIFO 형태
- 경우에 따라 우선순위를 부여할 수도 있으며 이땐 FIFO가 아님.
- 메시지를 받고 행동 방식을 변경하면, 변경된 행동방식은 다음 메시지를 처리할  때부터 적용

### 2.3.5. 자식 액터

* 자식들의 목록은 액터의 컨텍스트가 관리하며 액터는 컨텍스트를 통해 자식 목록에 접근 가능

### 2.3.6. 감독 정책

* 자식의 실패를 처리하기 위한 액터의 정책
* 액터는 하나의 정책만 가질 수 있다.
* 자식 액터에 대해 서로 다른 정책을 적용하려면, 자식을 그룹화하여 해당 정책을 가진 중간 수퍼바이저를 두면 된다. 이것이 액터 시스템의 구조화 방식

### 2.3.7. 액터의 종료

* 액터가 정상종료되면 액터의 리소스를 해지, 메일박스의 메시지를 dead letter 메일박스로 버린다.
* 이후에 수신되는 메시지들은 DeadLetter가 되고, 이는 테스트에 사용할 수도 있다.

## 2.4. 수퍼바이저와 모니터링

### 2.4.1. 수퍼바이저가 의미하는 것

수퍼바이저는 하위 계층으로 업무를 위임하고 하위 계층에서 발생한 실패에 대해 대응해야 한다. 스스로 자신과 자신의 하위 계층을 일시중지suspend하고 수퍼바이저로 메시지를 보내서 실패를 보고한다. 수퍼바이저의 대응 방식은 다음과 같다.

* 하부 계층을 재개resume한다.
* 하부 계층을 재시작restart한다.
* 하부계층을 영구적으로 중지stop한다.
* 실패를 상위로 보고escalate한다. 이 실패는 자신의 실패가 된다.

Actor 클래서의 preStart 훅hook의 디폴트 구현은 재시작에 앞서 모든 자식들을 중지시키도록 되어있다. 이 훅이 실행된 후 모든 자식은 재귀적으로 재시작restart한다.

감독 기능은 재귀적인 실패 처리 구조를 구성하는 것이 핵심이므로 하나의 계층에서 너무 많은 것을 하는 것은 좋지 않다.

최상위 액터는 라이브러리에 의해 만들어지며 그 외의 액터는 다른 액터에 의해서만 만들어진다.

### 2.4.2. 최상위 수퍼바이저

### 2.4.3. 재시작이 의미하는 것

### 2.4.4. 생명주기 모니터링

### 2.4.5. One-For-One Strategy vs. All-For-One Strategy

## 2.5. 액터 레퍼런스, 경로, 주소

### 2.5.1. 액터 레퍼런스

### 2.5.2. 액터 경로

```scala
akka://mysystem/user/testy/kiddo
```

#### 액터 레퍼런스와 경로의 차이점

* 액터 레퍼런스 : 하나의 액터를 가리키며, 생명주기는 액터의 생명주기와 일치
* 액터 경로 : 액터의 존재 여부와 상관 없이 액터의 이름을 표현

#### 액터 경로의 앵커

#### 로컬 액터 경로

#### 물리적 액터 경로

#### 액터 경로 앨리어스 또는 심볼릭 링크



### 3.1.5. 메시지 발신

액터에 메시지를 보내는 두 가지 방법

* `!` `tell` 비동기asynchronously로 메시지를 보내고 즉시 리턴 (보내고 잊기fire-and-forget)
* `?` `ask` 동기synchronously로 메시지를 보내고 응답한 Future를 반환.

#### Tell: Fire-forget



## 3.4. 디스패처

모든 MessageDispatcher는 ExecutionContext 를 구현하고 있으므로 퓨쳐Future 같은 임의의 코드를 실행하는데 사용할 수 있다.



## 3.6. 라우팅

[🔗참조문서](https://sslee05.github.io/blog/2018/04/27/akka-router-01/)

메시지를 라우터router를 통해 보내면 라우티routee인 목적지 액터에 효율적으로 전달할 수 있다.
여러 개의 라우티들을 두어 성능 향상에 목적을 둔다.
같은 메시지를 라우티들에 동시 발송하여 가장 빠른 응답의 경쟁 관계?

### 3.6.1. 간단한 라우터

```scala
import akka.routing.{ ActorRefRoutee, RoundRobinRoutingLogic, Router }

class Master extends Actor {
    var router = {
        val routees = Vector.fill(5) {
            val r = context.actorOf(Props[Worker])
            context watch r
            ActorRefRoutee(r)
        }
        Router(RoundRobinRoutingLogic(), routees)
    }

    def receive = {
        case w: Work =>
            router.route(w, sender())
        case Terminated(a) =>
            router = router.removeRoutee(a)
            val r = context.actorOf(Props[Worker])
            context watch r
            router = router.addRoutee(r)
    }
}
```

### 3.6.2. 라우터 액터

라우티를 직접 관리, 라우팅 로직을 로드하고 설정을 읽어오는 라우터 액터

* Pool: 라우터가 라우티를 자식액터로 만들고, 자식이 종료되면 라우터에서 제거
* Group: 외부에서 라우티 액터를 만들어 라우터에 추가. 라우터는 액터 셀렉션을 이용하여 지정된 경로로 메시지 전송

#### Pool

routee의 life cycle 관리.
router가 생성한 자식이 routee가 됨.

라운드로빈round-robin 방식으로 메시지를 Worker 라우티로 전달하는 라우터.

```scala
val router2: ActorRef = context.actorOf(RoundRobinPool(5).props(Props[Workder]), "router2")
```

#### Group

routee의 life cycle을 관리하는 actor를 만들고 이를 group actor와 통신한다. 이를 통해 group actor에게 message를 보내어 라우티를 추가, 삭제한다.











# 7. 퓨처와 에이전트

## 7.1. 퓨처

### 7.1.1. 소개

동시성 작업의 결과를 받아오는데 사용되는 데이터 구조. 동기(블로킹) & 비동기 두 가지 방법으로 작업의 결과를 받아올 수 있다.

### 7.1.2. 실행 컨텍스트

퓨처는 ExecutionContext가 필요하다. 각각의 액터는 MessageDispacher 위에서 동작하며 이 디스패처는 ExecutionContext의 역할을 한다.

### 7.1.3. 액터와 함께 사용

### 7.1.4. 직접 사용

액터로부터 응답을 수신하는 방법

* 메시지를 보내고(actor ! msg) 응답 메시지 수신
* Actor의 ? 메서드로 메시지를 보내서 리턴 받은 Future를 사용

Actor가 반환하는 Future 타입은 동적이다. 👉 Future[Any] 타입 👉 asInstanceOf 사용

```scala
val future = actor ? msg
val result = Await.result(future, timeout.duration).asInstanceOf[String]
```

퓨처의 결과를 어떤 액터로 보내려면 파이프를 사용한다.





future promise monad




