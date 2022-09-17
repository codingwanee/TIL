# akka-quartz-scheduler
- Akka document가 제공하는 설명을 번역한 내용
- 원문 글 : https://github.com/enragedginger/akka-quartz-scheduler


## (들어가기 전에) 내가 Quartz에 대해 간단히 조사한 내용
- Terracotta라는 회사에 의해 개발된 Job Scheduling 라이브러리
- 배치 프로그램을 만들때 사용하는 라이브러리
- 완전히 자바로 개발되어 어느 자바 프로그램에서도 쉽게 통합해서 개발할 수 있다.
- 수십~수천 개의 작업도 실행 가능하며, 간단한 Interval 형식이나 cron 표현식으로 복잡한 스케줄링도 지원
- 참고글: https://advenoh.tistory.com/51

#### 장점
- DB 기반으로 스케줄러 간의 Clustering 기능을 제공
- 시스템 fail-over와 random 방식의 로드 분산처리를 지원
- in-memory job scheduler도 제공
- 여러 기본 plug-in 제공
    - ShutdownHookPlugin - JVM 종료 이벤트를 캐치해서 스케줄러에게 종료 알려줌 
    - LoggingJobHistoryPlugin - Job 실행에 대한 로그를 남겨 디버깅할 때 유용하게 사용할 수 있다.

#### 단점
- Clustering 기능을 제공하지만, 단순한 random 방식이라 완벽한 Cluster 간의 로드 분산은 할 수 없음
- 스케줄링 실행에 대한 History는 보관하지 않는다.
- Fixed Delay 타입을 보장하지 않으므로 추가 작업이 필요하다.



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

- `scheduler` 인스턴스에서 가장 기본이 되는 외부 메소드는 `schedule`이며, 다음과 같이 잡 스케줄링에 이용된다: 
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
        - A typed ActorRef[T], who will be sent msg each time the schedule fires
    - msg
        - An instance of A, representing a message instance which will be sent to receiver each time the schedule fires
    - startDate
        - An optional Date, for postponed start of a job. Defaults to now.


- `scheduleTyped[A]`을 호출하면 `java.util.Date` 객체가 리턴되는데, 이것은 새로 세팅된 스케줄이 발사되는 시간을 나타낸다.
- Quartz schedule이 실행을 유발(trigger fires)할 때마다, Quartz는 `msg`을 복제하여 당신의 `receiver` 액터에 보낸다.
- Akka Typed Actor에서도 예전 버전과 같은 로직으로 Quartz 스케줄링을 이용할 수 있단 것을 알아둬라.


## Akka Actor (2.5.x)
- 당신이 오래된 버전의(2.5.x 미만인) Akka Actor를 이용하고 있다면, Akka 프로젝트는 Scheduler를 생성하고 이용할 수 있게 한다:
```scala
// Akka Actors sample.
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

val scheduler = QuartzSchedulerExtension(system)
```
- 위 소스에서 `system`이란 Akka ActorSystem의 인스턴스를 가리킨다
- `QuartzSchedulerTypedExtension`이 `ActorSystem`의 스코프 내에 있으며, ActorSystem 당 오직 하나의 인스턴스만 존재한다는 것을 기억해라.

- `scheduler` 인스턴스에서 기본적인 외부 메소드는 `schedule`이며, 다음과 같이 잡 스케줄링에 이용된다: 
```scala
def scheduleTyped[T](name: String, receiver: ActorRef[T], msg: T): java.util.Date
```
or
```scala
def scheduleTyped[T](name: String, receiver: ActorRef[T], msg: T, startDate: Option[Date]): java.util.Date
```
- 각 스케줄의 인자는 다음과 같다:
    - name
        - schedule의 이름을 알려주는 String.
        - 설정에 명시된 이름과 일치해야 한다
    - receiver
        - 스케줄이 실행될 때마다 메시지를 받는 ActorRef 또는 ActorSelection
    - msg
        - 스케줄이 실행될 때마다 receiver에게 보내질 메세지 객체
    - startDate
        - An optional Date, for postponed start of a job. Defaults to now.
- `schedule`을 실행시키면 `java.util.Date` 객체가 반환되며, 이것은 새로 생성된 스케줄이 처음 실행되는 시간을 나타낸다.
- Quartz schedule의 실행이 유발될 때마다 Quartz는 당신의 `receiver` 액터에 `msg`를 전송한다.

- 아래는 `Every30Seconds`라고 불리는 스케줄을 이용해 `Tick` 메세지를 `CleanupActor`를 보내는 예제이다.
```scala
case object Tick

val cleaner = system.actorOf(Props[CleanupActor])

QuartzSchedulerExtension(system).schedule("Every30Seconds", cleaner, Tick)
```
- `Tick` 메세지가 핸들링되는 곳은 일반적으로 Actor의 메세지 루프 안이다.
- 만약 스케줄 메세지들이 "일반적인" 액터 메시지보다 확실히 더 즉각적으로 다뤄지길 바란다면, **Priority Mailboxes**를 활용할 수도 있다.
- 좀더 구체적인 내용은 아래 'Schedule Configuration' 섹션에 있다

## Returning scheduled Fire Time
- 실행시간을 아는 것이 도움이 될 때가 있다.
- 예를들면, 에러가 밸생했을 때 우리는 job trigger가 실행되고 다시 복구될 수 있다는 것을 알 수 있다.

- 예시로 아래 `Tick` 메세지를 감싸고 있는 `MessageRequireFireTime` 케이스 클래스를 보자.
- 이 소스는 `WorkerActor`에게 `MessageWithFireTime(Tick, previousFireTime, scheduledFireTime, nextFireTime)` 메세지를 전송한다.

```Scala
case object Tick

val worker = system.actorOf(Props[WorkerActor])

QuartzSchedulerExtension(system).schedule("Every30Seconds", worker, MessageRequireFireTime(Tick))
```

## Configuration of Quartz Scheduler
- `akka-quartz-scheduler`의 모든 설정은 `akka.quartz` 설정블록 안에 있는 Akka 설정 내부에 들어있다.
- Akka 설정파일과 마찬가지로 이 설정도 **[HOCON Configuration Format](https://github.com/lightbend/config/blob/master/HOCON.md)**을 따른다.
- 또한, `foo.bar.baz = x` 로 설정된 내용들은 모두 `foo { bar { baz = x }}` 형태로도 표현할 수 있다.

- 설정의 최고 레벨에서 옵션값들을 설정하면 디폴트값을 덮어쓸 수 있다.
    - `defaultTimezone` - [String] / 디폴트 UTC
        - 모든 job들이 실행되는 타임존을 나타낸다.
        - java.util.TimeZone.getTimeZone()으로 파싱할 수 있어야 한다.
    - `threadPool.threadCount` - [Int] / 디폴트 1
        - Quartz 스레드풀 내부에 위치한 스레드의 개수
        - 실행해야 할 스케줄이 많다면 숫자를 늘리고 싶을 것이다. 
        - 스레드가 1개만 있다면 트리거는 작업들을 queue에 쌓아놓고 알림을 주지는 않는다
  
    - `threadPool.threadPriority` - [Int] / 디폴트 5
        - Quartz의 스레드를 할당하는 우선순위
        - 1부터(최저 우선순위) 10까지(최고 우선순위) 지정 가능
    - `threadPool.daemonThreads` - [Boolean] / 디폴트 TRUE
        - 스레드가 Quartz를 생성하고나서 데몬 스레드로 실행할지 여부

- `akka.quartz` 설정에는 두개의 '주(primary)' 서브-블록이 있는데, 바로 `schedules`와 `calendars`이다.

#### Schedule Configuration
- Schedules는 Quartz의 잡과 트리거에 관해 뽑아낸 컨셉이다.
- 스케줄 된 이벤트를 발생시키는(코드에 설정된 대로 actor에게 메세지를 보내는) 이름이 부여된 schedule을 정의할 수 있도록 해준다.
- 현재는 "Cron" 스케줄만 정의할 수 있다. 이용법은 표준 Unix cron syntax에 약간의 추가를 가미한 **[Quartz' CronExpression Lanquage](http://www.quartz-scheduler.org/api/2.1.7/org/quartz/CronExpression.html)** 규칙을 따른다.

- 설정 중에서 스케줄명은 `schedule`이 호출되었을 때 요청된 job과 매치시키는 데 사용된다.
- 하지만 케이스는 "매칭되는 잡이 있는가"에 대해서는 관여하지 않는다. 설정 확인은 케이스에 둔감하다.

- `akka.quartz.schedules` 안에 있는 스케줄에 대한 설정 블록에서 이름 블록 내부의 서브 요소들은, 예를들어 `Every30Seconds`와 같은 요소는 `akka.quartz.schedules.Every30Seconds` 블록 안에 정의되어 있다.
- schedule 설정 안에 올 수 있는 요소들은 다음과 같다:
    - expression - [String] [required]
        - 언제 잡이 실행될지 나타내는 쿼츠 크론식을 검증한다.
        - ex [expression = "*/30 * * ? * *"] : 매일 매 30초 마다 실행
        - 하지만, 이 표현식으로 생성된 실행 스케줄은 calendars 변수로 수정할 수 있다.
    - timezone - [String] [optional] / 디폴트 UTC
        - 스케줄을 실행하는 타임존
        - 디폴트는 akka.quartz.defaultTimezone에 있는 UTC를 따른다.
        - java.util.TimeZone.getTimeZone() 으로 파싱할 수 있다.
    - description - [String] [optional] / 디폴트 null
        - 잡에 대한 설명
        - 대부분 "이게 무엇을 위한 스케줄인지"를 사람이 쓰는 언어로 적어놓는 게 목적이다.
        - 하지만 종종 디버그를 위해 Quartz 안에 스케줄러 내용을 덤프로 넣어둘 수도 있다.
    - calendar - [String] [optional] / 디폴트 NONE (String)
        - 설정된 Calendar의 이름을 String으로 적는 옵션
        - 이 Calendar는 "제외"되는 목록으로써 schedule에 제공된다
        - Calendar에 들어간 시간/날짜는 스케줄 실행에서 제외된다
        - ex. Calendar에 매주 월요일이 제외한 매 시간 단위라고 되어있다면, 스케줄은 *매주 월요일마다* 실행된다.
        - NOTE: 1.3.x과 그 이전 버전에서는 이 속성은 "calendars"라는 이름으로 String 목록을 제공했다. 하지만, Quartz는 이 라이브러리가 실제로 한 스케줄에 대해 여러 calendars를 제공하는 것은 아니었다. 따라서 1.4.x 버전부터는 이 속성을 "calendar"라고 이름을 다시 붙이고 optional String으로 변경했다.

- 아래는 `Every30Seconds`라고 불리는 스케줄애 대한 예시이다:
```properties
akka {
  quartz {
    schedules {
      Every30Seconds {
        description = "A cron job that fires off every 30 seconds"
        expression = "*/30 * * ? * *"
        calendar = "OnlyBusinessHours"
      }
    }
  }
}
```
- 이 Schedule은 크론 표현식으로 매일, 매 30초마다 실행하도록 설정되어 있다.
- 하지만 "OnlyBusinessHours" 캘린더에 의해 수정되었고, 여기에 따라서 8am 부터 6pm까지는 제외하고 실행되게 된다(아래에 세부사항이 있다)

#### Calendar Configuration
- `akka-quartz-scheduler` 안에 있는 Calendars는 **[Quartz' Calendars](http://www.quartz-scheduler.org/documentation/quartz-2.x/tutorials/tutorial-lesson-04)**를 따라한 컨셉이다.
- 구체적으로는, schedule을 오버라이드 해서 *제외할 내용*을 설정할 수 있게 한다.

- Calendars는 `akka.quartz.calendars` 설정 블록에 위치하여 글로벌 설정에 적용된다.
- 디폴트로는 Schedule에 아무런 Calendars도 제공되지 않는다.
- 대신, 위에서 보여진 대로 Schedule 설정의 `calendars` 배열의 안에 Calendar의 이름을 적어 설정한다.

- `akka.quartz.calendars` 안에서 calendars를 설정하는 설정블록은 명명된 블록 안에서 서브 요소들, 예를 들어 `OnlyBusinessHours`라는 이름의 calendar는 `akka.quartz.calendars.OnlyBusinessHours`라는 설정블록 안에 값이 정의되어 있다.

- Calendar에는 몇몇 타입이 있는데 제각기 다른 설정을 가지고 있다.
- *모든* 타입의 Calendar에 적용되는 설정값들은 아래와 같다:
    - type - [String] [required]
        - Calendar의 타입을 검증
        - 현재 제공하는 목록: Annual, Holiday, Daily, Monthly, Weekly, and Cron
        - 스케줄을 실행하는 타임존
        - 디폴트는 akka.quartz.defaultTimezone에 있는 UTC를 따른다.
        - java.util.TimeZone.getTimeZone() 으로 파싱할 수 있다.
    - description - [String] [optional] / 디폴트 null
        - 잡에 대한 설명
        - 대부분 "이게 무엇을 위한 스케줄인지"를 사람이 쓰는 언어로 적어놓는 게 목적이다.
        - 하지만 종종 디버그를 위해 Quartz 안에 스케줄러 내용을 덤프로 넣어둘 수도 있다.

- 그리고 각 Calendar 타입별로 갖는 세부 설정 요소들은 ...

#### 'Annual' Calendar

- 해당 연도의 특정 일자를 제외시키는 연단위 calendar
- 예를 들어, 매년 발생하는 은행 휴일(크리스마스, 새해 등)을 제외한다.
- 이 달력은 계정에 연도를 포함시키지 *않으며*, 모든 연도에 적용된다.

- 



#### 'Holiday' Calendar

#### 'Daily' Calendar

#### 'Monthly' Calendar

#### 'Weekly' Calendar

#### 'Cron' Calendar


## Dynamic create, update, delete `JobSchedule` operations
- `JobSchedule` 오퍼레이션들은 프로그램적으로 job과 job scheduling을 한 번에 관리할 수 있게 한다.
- *동작 예시에 대해서는 다음 test section을 참고하자*: "The Quartz Scheduling Extension with Dynamic create, update, delete JobSchedule operations" in com.typesafe.akka.extension.quartz.QuartzSchedulerFunctionalSpec

#### Create JobSchedule
- `createJobSchedule`는 새로운 job을 생성하고 스케줄링 할 수 있도록 해준다:
```scala
val scheduleJobName : String = "myJobName_1"
val messageReceiverActor: ActorRef = myActorRef
val messageSentToReceiver : AnyRef = myCaseClassMessage 
val scheduleCronExpression: String = "*/10 * * ? * *" // Will fire every ten seconds

try { 
  scheduler.createJobSchedule(
  	name = scheduleJobName, 
  	receiver = messageReceiverActor, 
  	msg = messageSentToReceiver, 
  	cronExpression = scheduleCronExpression)
} catch {
  case iae: IllegalArgumentException => iae // Do something useful with it.
}	
```

- 추가적으로, 아래의 optional description, calendar, timezone 파라미터를 설정할 수 있다:
```scala
val scheduleJobDescriptionOpt : Option[String] = Some("Scheduled job for test purposes.")
val aCalendarName: Option[String] = Some("HourOfTheWolf")
val aTimeZone: java.util.TimeZone = java.util.TimeZone.getTimeZone("UTC")

try { 
  scheduler.createJobSchedule(
  	name = scheduleJobName, 
  	receiver = messageReceiverActor, 
  	msg = messageSentToReceiver, 
  	cronExpression = scheduleCronExpression, 
  	description = Some(job.description), 
  	calendar = aCalendarName, 
  	timezone = aTimeZone
  )
} catch {
  case iae: IllegalArgumentException => iae // Do something useful with it.
}	
```

#### Update JobSchedule
- `updateJobSchedule`은 JobSchedule을 생성하는 기능과 정확히 같은 시그니처를 갖지만, 기존에 있는 `scheduleJobName`을 업데이트 하는 것이 다르다.
```scala
try { 
  scheduler.updateJobSchedule(
  	name = scheduleJobName, 
  	receiver = messageReceiverActor, 
  	msg = messageSentToReceiver, 
  	cronExpression = scheduleCronExpression, 
  	description = Some(job.description), 
  	calendar = aCalendarName, 
  	timezone = aTimeZone
  )
} catch {
  case iae: IllegalArgumentException => iae // Do something useful with it.
}	
```

#### Delete JobSchedule
```scala
try {
  if (scheduler.deleteJobSchedule(name = scheduleJobName)) { 
    // Do something if deletion succeeded
  } else {
    // Do something else if deletion failed
  }
} catch {
  case e: Exception =>
    // Take action in case an exception is thrown 
}
```

## Mass operations suspendAll, resumeAll, deleteAll
- 이 대량 오퍼레이션들은 많은 수의 `jobSchedule`을 한번에 관리할 수 있게 한다.
- *동작 예시는 다음 test section을 참고하도록 한다*: "The Quartz Scheduling Extension with Dynamic mass methods" in com.typesafe.akka.extension.quartz.QuartzSchedulerFunctionalSpec

#### Suspend All Schedules
- 스케줄러에 있는 모든 job들을 유예(일시정지)한다.
```scala
try {
  scheduler.suspendAll()
} catch {
  case e: SchedulerException =>
    // Take action in case an exception is thrown 
}
```

#### Resume All Schedules
- 스케줄러에 있는 모든 일시정지된 job들을 재기동한다.
```scala
try {
  scheduler.resumeAll()
} catch {
  case e: SchedulerException =>
    // Take action in case an exception is thrown 
}
```

#### Delete All Schedules
- 스케줄러를 셧다운 하지 않고 스케줄러에 있는 job을 모두 삭제한다.
```scala
try {
  scheduler.deleteAll()
} catch {
  case e: SchedulerException =>
    // Take action in case an exception is thrown 
}
```
