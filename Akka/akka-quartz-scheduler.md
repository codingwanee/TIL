# akka-quartz-scheduler
- Akka document가 제공하는 설명을 번역한 내용
- 원문 글 : https://github.com/enragedginger/akka-quartz-scheduler


## intro
- 이 글은 Akka 2.6.x에서 Quartz의 확장기능과 스케줄링 기능을 이용하는 방법을 담았다.

- (2022.09 기준) 현재 Maven Central에서 이용 가능한 최신 배포판은 Scala 2.13.x와 Akka.2.6.x이다.
- 만약 이 외 다른 버전의 Scala와 Akka 조합을 이용하고 있다면 issue 페이지에 글을 남기도록 하자. (왜 당신의 현재 버전에서 작동하지 않는 이유도 함께 적어달라. 나는 이것들에 관심이 많다.)

## Why Akka and Quartz?
- akka-quartz-scheduler 는 Quartz의 완벽한 버전이 *아니다*.
- 대신, 우리는 Quartz 스케줄링 시스템의 컨셉을 기능화하여 기존보다 더 견고하고 신뢰갈 만한 스케줄링 컴포넌트를 Akka에 제공하려 한다.

- 여기서 목표는 Akka에 스케줄링 시스템을 제공하여 Cron 타입 job에 더욱 친화적이 되고자 하는 것이다.
- 즉 Quartz로 이루어지는 장시간 주기의 ActorSystem을 설정하는 것이다.

- 지금 당장은 분산 트랜젝션, 영속성, 클러스터링, 또는 다른 어떤 것들에 대한 계획은 없다.
- 이것들은 cron-ey 타입의 타이밍과 스케줄링에 적합한 것들이다.

- Quartz에는 JobDataMaps을 지나쳐 job ticks를 가로질러 가변상태를 발생하는 능력이 있다 (?)
- There is the ability in Quartz to pass JobDataMaps around that accrue mutable state across job ticks; 
- 우리는 현재 무결성 시행을 지원하지 *않는다*.
- 하지만 필요하다면 추후에 좀더 깊은 액터 구조를 통해 비슷한 기능을 제공할 수도 있다.

## Why Not use $OtherComparableTool Instead?
1. 
    - 기존에 이미 존재하고 있던 Akka Scheduler를 이용하는 게 뭐가 어떻다고 그러는가? Viktor Klang이 지적했듯이, '아마도 "Scheduler"라는 이름이 원흉이었고, "Deferer(지연)"가 좀더 적합했을 것 같다.'
    - Akka Scheduler는 현재로부터 지연시간(duration)을 가진 뒤 이벤트를 발생시키는 기능으로 설계되었다: 당신은 "이 job을 지금으로부터 15분 뒤에 발생시키고, 또 그로부터 매 30분마다 지속 발생시켜라" 라고 할 수는 있지만 "이 job을 매일 3pm에 발생시켜줘"라고는 말할 수 없다.
    - 게다가, Akka의 기본 스케줄러는 `HashedWheelTimer` 위에서 실행되는데, - 엄밀히 말하면 실행 시간에 대해 확실한 보장을 해주지 못하기 때문에 잠재적으로 job의 누실을 발생시킨다.

 2. 
    - 그럼 그냥 Akka의 Camel Extention에 있는 Quartz 컴포넌트를 갖다 쓰면 안되는 것인가?
    - i. 일단 시작부터, Akka의 Camel extention은 Akka의 2.0.x 버전에서는 쓸 수 없다. 오직 2.1 이상 버전에서만 동작한다.
    - ii. Camel은 전체 구조를 변경해야 하게 만들 수 있다(Consumer, Producer 등) 또한, Quartz 지원만 필요한 사람에게는 "경량" 플러그인이 아니다.
    - iii. 우리는 Quartz의 컨셉을 가져다 Akka에 집어넣어서 좀더 네이티브 환경에 바로 쓸 수 있을 뿐만 아니라 가벼운 느낌으로 이용하게 하고 싶었다.

3. 
    - GitHub에 올라와있는 다른 `akka-quartz` 컴포넌트는 어떤가?
    - 앞서말했던 `akka-quartz` 컴포넌트는 Actor를 통해 동작한다
        - Quartz Scheduler를 가지고 있는 Actor의 인스턴스를 생성
        - 위에서 만든 Actor에 메세지를 전송해서 스케줄된 잡을 실행 
        - Actor는 "싱글톤"스러운 보장을 해주지 않기 때문에, 사용자가 실수로 쓰레드풀에서 여러개의 중복된 스케줄러 인스턴스를 스핀업하기 쉽다.
    - 따라서 대신 `akka-quartz-scheduler`를 이용하면 하나의 `ActorSystem` 당 오로지 하나의 Quartz Scheduler를 제공하는 것을 보장한다.
    - 이 말은 어떤 ActorSystem에 대해서도 사이즈를 조절할 수 있는 싱글 쓰레드풀 하나만 만들어주고, 다른 것들은 만들어주지 않는다는 뜻이다.

- 마지막으로, 위에 언급된 대체 기능들은 반복 스케줄 등을 코드에서 분리해서 설정하기가 쉽지 않다.(그러면 개발자 말고 운영자가 고생한다)

- 게다가, `akka-quartz-scheduler`는 코드 안에서 다음 것들만 명시하도록 허락한다: job 이름, 액터가 어디에 tick을 보낼지, tick에 어떤 메세지를 보낼지
- tick을 얼마나 자주 보낼지에 대한 설정은 Akka의 configuration file로 빠져서 외부화되어있다.

- 또한, 개발자는 그들의 코드 안에 반복적인 작업의 뼈대의 윤곽을 잡을 수 있다.
- 바로 **언제** 스케줄의 '틱'을 유발할지 명시하는 것이다.
- 그러면 운영자가 job이 어떤 주기로 발생하는지, 어떤 규칙을 따라 발생하는지를 마무리한다.

- 이것은, 다른 것보다도 테스트를 위해 스케줄을 수정하면서 발생하는 사고를 방지할 수 있다.
- 변경이 유연하고 소스 코드를 재컴파일 할 필요가 없기 때문이다.


## TODO
- investigate supporting listeners, with actor hookarounds.
- misfires and recovery model - play nice with supervision, deathwatch, etc [docs page 23 - very close to supervision strategy]

## Usage
- CHANGLOG.md 를 읽어보면 릴리즈 변경 목록을 확인할 수 있다.
- `akka-quartz-scheduler`를 사용하기 위해서는 먼저 당신의 SBT 프로젝트에 의존성을 추가해야 한다:
```scala
// For Akka 2.6.x and Akka Typed Actors 2.6.x and Scala 2.12.x, 2.13.x, 3.1.x
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.9.3-akka-2.6.x"

// For Akka 2.6.x and Scala 2.12.x, 2.13.x
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.5-akka-2.6.x"

// For Akka 2.5.x and Scala 2.11.x, 2.12.x, 2.13.x
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.8.1-akka-2.5.x"

// For Akka 2.4.x and Scala 2.11.x, 2.12.x
libraryDependencies += "com.enragedginger" %% "akka-quartz-scheduler" % "1.6.0-akka-2.4.x"
```
- Note: Akka 2.6부터, Scala 2.11은 더 이상 지원하지 않는다. 만약 최신 akka-quartz-scheduler를 이용하고 싶은데 Scala 2.11를 이용하고 있다면, issue/PR을 열면 도울 수 있는 방안을 찾아보겠다.

```scala
//Older versions of the artifact for those that require pre Akka 2.3 or pre Scala 2.11
//If you would like a current version of the artifact to be published for your required version
//of Akka and Scala, simply file on issue on the project page.
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"


// For Akka 2.0.x
libraryDependencies += "com.typesafe.akka" %% "akka-quartz-scheduler" % "1.2.0-akka-2.0.x"
// For Akka 2.1.x
libraryDependencies += "com.typesafe.akka" %% "akka-quartz-scheduler" % "1.2.0-akka-2.1.x"
// For Akka 2.2.x
libraryDependencies += "com.typesafe.akka" %% "akka-quartz-scheduler" % "1.2.0-akka-2.2.x"
// For Akka 2.3.x and Scala 2.10.4
libraryDependencies += "com.typesafe.akka" % "akka-quartz-scheduler_2.10" % "1.4.0-akka-2.3.x"
```
- Note: Akka revision (이전 릴리즈 버전들은 Akka )




## Akka Typed Actor (2.6.x)
- 당신이 최신 버전의 Akka Actor를 이용하고 있다면, Akka 프로젝트는 Typed Scheduler를 생성하고 이용할 수 있게 한다:
```scala
// Akka Typed Actors sample.
import com.typesafe.akka.extension.quartz.QuartzSchedulerTypedExtension

val scheduler = QuartzSchedulerTypedExtension(typedSystem)
```

- 위 소스에서 `typedSystem`이란 Akka Typed ActorSystem[-T]의 인스턴스를 가리킨다
- `QuartzSchedulerTypedExtension`이 `ActorSystem[-T]`의 스코프 내에 있으며, ActorSystem[-T] 당 오직 하나의 인스턴스만 존재한다는 것을 기억해라.

- `scheduler` 인스턴스에서 가장 처음 노출되는 메소드는 `schedule`이며, 다음과 같이 잡 스케줄링에 이용된다: 
```scala
def scheduleTyped[T](name: String, receiver: ActorRef[T], msg: T): java.util.Date
```
or
```scala
def scheduleTyped[T](name: String, receiver: ActorRef[T], msg: T, startDate: Option[Date]): java.util.Date
```
- 각 스케줄의 인자는 다음과 같다:
    - name
        - A String identifying the name of this schedule. This must match a schedule present in the configuration
    - receiver
        - A typed ActorRef[T] or ActorSelection, who will be sent msg each time the schedule fires
        - 스케줄이 발사(?)될 때마다 메시지를 받는 typed ActorRef[T]
    - msg
        - An AnyRef, representing a message instance which will be sent to receiver each time the schedule fires
    - startDate
        - An optional Date, for postponed start of a job. Defaults to now.





        

## Akka Actor (2.5.x)
- 당신이 최신 버전의 Akka Actor를 이용하고 있다면, Akka 프로젝트는 Scheduler를 생성하고 이용할 수 있게 한다:
```scala
// Akka Actors sample.
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

val scheduler = QuartzSchedulerExtension(system)
```
- 위 소스에서 `system`이란 Akka ActorSystem의 인스턴스를 가리킨다
- `QuartzSchedulerTypedExtension`이 `ActorSystem`의 스코프 내에 있으며, ActorSystem 당 오직 하나의 인스턴스만 존재한다는 것을 기억해라.

- `scheduler` 인스턴스에서 가장 처음 노출되는 메소드는 `schedule`이며, 다음과 같이 잡 스케줄링에 이용된다: 
```scala
def scheduleTyped[T](name: String, receiver: ActorRef[T], msg: T): java.util.Date
```

or

```scala
def scheduleTyped[T](name: String, receiver: ActorRef[T], msg: T, startDate: Option[Date]): java.util.Date
```
- 각 스케줄의 인자는 다음과 같다:
    - name
        - A String identifying the name of this schedule. This must match a schedule present in the configuration
    - receiver
        - An ActorRef or ActorSelection, who will be sent msg each time the schedule fires
        - 스케줄이 발사(?)될 때마다 메시지를 받는 ActorRef 또는 ActorSelection
    - msg
        - An AnyRef, representing a message instance which will be sent to receiver each time the schedule fires
    - startDate
        - An optional Date, for postponed start of a job. Defaults to now.




## Quartz Job Scheduler with Akka

- If you use old Akka Actor version then, from within your Akka project you can create and access a Scheduler:
- 오래된(2.6.x 미만인) 버전의 아카 액터를 쓴다면, 아카 프로젝트에서는 스케줄러를 생성하고 접근할 수 있다.
```scala
// Akka Actors sample.
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

val scheduler = QuartzSchedulerExtension(system)
```

- 위 코드에서 `system`이란 Akka `ActorSystem`의 인스턴스를 가리키며, 아래와 같이 job을 스케줄링 하는 데 쓰인다:
```scala
def schedule(name: String, receiver: ActorRef, msg: AnyRef, startDate: Option[Date]): java.util.Date
```
or
```scala
def schedule(name: String, receiver: ActorSelection, msg: AnyRef, startDate: Option[Date]): java.util.Date
```

- 각 스케줄의 인자는 다음과 같다:
    - name
        - A String identifying the name of this schedule. This must match a schedule present in the configuration
    - receiver
        - An ActorRef or ActorSelection, who will be sent msg each time the schedule fires
        - 스케줄이 발사(?)될 때마다 메시지를 받는 ActorRef 또는 ActorSelection
    - msg
        - An AnyRef, representing a message instance which will be sent to receiver each time the schedule fires
    - startDate
        - An optional Date, for postponed start of a job. Defaults to now.

- `schedule`이 발생하면 `java.util.Date`의 인스턴스가 반환됨
- 매 시간마다 Quartz schedule은 작업을 실행하고, Quartz는 msg의 복제해서 `receiver` 액터에 보낸다
- 다음 `Every30Seconds` 스케줄 예제를 보면, `Tick` 메세지를 `CleanupActor`에게 보내고 있다.(클린 작업을 하도록 손을 흔드는 역할이다)

```Scala
case object Tick

val cleaner = system.actorOf(Props[CleanupActor])

QuartzSchedulerExtension(system).schedule("Every30Seconds", cleaner, Tick)
```

- `Tick` 메세지가 위치한 곳은 액터 메시지 loop 안에서 정상적으로 동작하는 곳이다.
- 만약 "일반적인" 액터 메시지보다 좀더 즉각적으로 스케줄 메시지가 처리되길 원한다면 **Priority Mailboxes**를 활용할 수 있다.

- 좀더 구체적인 내용은 아래 'Schedule Configuration' 섹션에 있다


#### 지정된 스케줄 시간을 반환하기
- 실행시간을 아는 것이 도움이 될 때가 있다.
- 예를들면, 에러가 밸생했을 때 우리는 job trigger가 실행되고 다시 복구될 수 있다는 것을 알 수 있다.

- 예시로 아래 `Tick` 메세지를 감싸고 있는 `MessageRequireFireTime` 케이스 클래스를 보자.
- 이 소스는 `WorkerActor`에게 `MessageWithFireTime(Tick, previousFireTime, scheduledFireTime, nextFireTime)` 메세지를 전송한다.

```Scala
case object Tick

val worker = system.actorOf(Props[WorkerActor])

QuartzSchedulerExtension(system).schedule("Every30Seconds", worker, MessageRequireFireTime(Tick))
```
