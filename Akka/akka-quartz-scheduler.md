# Quartz Job Scheduler
- Akka document가 제공하는 설명을 번역한 내용
- 원문 글 : https://github.com/enragedginger/akka-quartz-scheduler





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

각 스케줄의 인자는 다음과 같다:
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






