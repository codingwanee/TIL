# sbt by example
- https://www.scala-sbt.org/1.x/docs/sbt-by-example.html 에 대한 번역 및 정리
- 정식 번역이 아니므로 의역 및 오역, 재정리 된 내용이 있음

## 도입부
- 본 내용은 sbt를 설치했다는 가정 하에 작성되었다.
- sbt가 어떻게, 왜 동작하는지에 앞서 예시를 보며 시작해보도록 하자.


## 기본적인 sbt 빌드
```shell
$ mkdir foo-build
$ cd foo-build
$ touch build.sbt
```


## sbt shell 시작하기
```shell
$ sbt
[info] Updated file /tmp/foo-build/project/build.properties: set sbt.version to 1.1.4
[info] Loading project definition from /tmp/foo-build/project
[info] Loading settings from build.sbt ...
[info] Set current project to foo-build (in build file:/tmp/foo-build/)
[info] sbt server started at local:///Users/eed3si9n/.sbt/1.0/server/abc4fb6c89985a00fd95/sock
sbt:foo-build>
```

## sbt shell 종료하기
- sbt shell을 마칠 때는 **exit**라고 입력하거나 Ctrl+C (mac) or Ctrl+D (Unix) or Ctrl+Z (Windows) 를 입력
```shell
sbt:foo-build> exit
```

## 프로젝트 컴파일하기
- sbt 인터액티브 쉘을 이용한다는 컨벤션으로 우리는 `sbt:...>` 또는 `>` 프롬프트를 이용한다.
```shell
$ sbt
sbt:foo-build> compile
```

## 코드 변경사항 리컴파일
- `compile` 접두어에 `~`을 붙인 `~compile`는 소스파일 내용이 수정되는 경우 즉시 재실행시키는 명령어이다.
```shell
sbt:foo-build> ~compile
[success] Total time: 0 s, completed May 6, 2018 3:52:08 PM
1. Waiting for source changes... (press enter to interrupt)
```

## 소스파일 생성
- 이제 위의 `~compile` 명령이 실행되고 있도록 놔두자.
- 다른 쉘이나 파일 매니저를 열어 위에서 생성한 foo-build 디렉토리 하위에 다음 중첩 디렉토리를 생성한다: `src/main/scala/example`
- 그리고 example 디렉토리 하위에 Hello.scala 를 생성한다. 자신이 좋아하는 에디터로 아래와 같이 코드도 작성해본다.
```scala
package example

object Hello {
  def main(args: Array[String]): Unit = {
    println("Hello")
  }
}
```
- 아까 실행시킨 명령에서 방금 새 파일을 만든 내용이 등장하는 것을 확인한다.
```log
[info] Compiling 1 Scala source to /tmp/foo-build/target/scala-2.12/classes ...
[info] Done compiling.
[success] Total time: 2 s, completed May 6, 2018 3:53:42 PM
2. Waiting for source changes... (press enter to interrupt)
```
- 엔터키를 눌러 `~compile` 을 종료한다.

## 이전 명령어 실행
- sbt shell에서 윗방향 화살표를 두번 눌러 실습 처음에 실행했던 `compile` 명령어를 찾는다

```shell
sbt:foo-build> compile
```

## 도움말 보는 법
- `help` 명령어를 이용해서 sbt 명령어에서 사용 가능한 기본 내용을 확인한다.
```shell
sbt:foo-build> help

  about      Displays basic information about sbt and the build.
  tasks      Lists the tasks defined for the current project.
  settings   Lists the settings defined for the current project.
  reload     (Re)loads the current project or changes to plugins project or returns from it.
  new        Creates a new sbt build.
  projects   Lists the names of available projects or temporarily adds/removes extra builds to the session.
  project    Displays the current project or changes to the provided `project`.

....
```


## 어플리케이션 실행하기
```shell
sbt:foo-build> run
[info] Packaging /tmp/foo-build/target/scala-2.12/foo-build_2.12-0.1.0-SNAPSHOT.jar ...
[info] Done packaging.
[info] Running example.Hello
Hello
[success] Total time: 1 s, completed May 6, 2018 4:10:44 PM
```

## sbt shell에서 ThisBuild / scalaVersion 세팅하기

#### 세팅
```shell
sbt:foo-build> set ThisBuild / scalaVersion := "2.13.8"
[info] Defining ThisBuild / scalaVersion
```

#### 세팅 확인
```shell
sbt:foo-build> scalaVersion
[info] 2.13.8
```


#### build.sbt에 세션 저장하기
- `session save` 명령어로 ad-hoc setting을 저장할 수 있다.
    - *ad-hoc*
        1. 라틴어로 "이것을 위해" 즉 "특별한 목적을 위해서"라는 뜻
        2. 일반화 할 수 없는, 특정한 문제나 일을 위해 만들어진 관습적인 해결책
        3. **분산형 무선통신 네트워크**로, 특정 기지국에 의존하지 않고 무서너 이동단말로만 구성된 네트워크
```shell
sbt:foo-build> session save
[info] Reapplying settings...
```
- 이제 `build.sbt` 파일은 아래 내용을 포함해야 한다.
```sbt
ThisBuild / scalaVersion := "2.13.8"
```

## 프로젝트에 이름 정하기
- 에디터를 이용해 build.sbt를 아래와 같이 변경할 수 있다.

```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

lazy val hello = (project in file("."))
  .settings(
    name := "Hello"
  )
```


## 빌드 리로드
- `reload` 명령어는 빌드를 리로드 할 수 있다.
- 이 명령어는 `build.sbt` 파일을 다시 읽어 변경된 세팅이 적용되도록 한다.

```shell
sbt:foo-build> reload
[info] Loading project definition from /tmp/foo-build/project
[info] Loading settings from build.sbt ...
[info] Set current project to Hello (in build file:/tmp/foo-build/)
sbt:Hello>
```
- 이제 프롬프트가 `sbt:Hello>`로 변경된 것을 확인할 수 있다.


## libraryDependencies에 ScalaTest 추가하기
- 에디터로 `build.sbt`를 다음과 같이 수정한다

```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

lazy val hello = (project in file("."))
  .settings(
    name := "Hello",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  )
```

- `reload` 명령어로 `build.sbt` 파일의 변경사항을 반영한다.
```shell
sbt:Hello> reload
```

## 테스트 실행
```shell
sbt:Hello> test
```

## 지속적인 실행 테스트
```shell
sbt:Hello> ~testQuick
```

## 테스트 작성하기
- 위 명령어가 실행되는 동안 에디터로 다음 파일을 생성한다: `src/test/scala/HelloSpec.scala`
```scala
import org.scalatest.funsuite._

class HelloSpec extends AnyFunSuite {
  test("Hello should start with H") {
    assert("hello".startsWith("H"))
  }
}
```
- `~testQuick` 으로 수정사항이 잡히는 것을 확인한다:
```shell
2. Waiting for source changes... (press enter to interrupt)
[info] Compiling 1 Scala source to /tmp/foo-build/target/scala-2.12/test-classes ...
[info] Done compiling.
[info] HelloSpec:
[info] - Hello should start with H *** FAILED ***
[info]   assert("hello".startsWith("H"))
[info]          |       |          |
[info]          "hello" false      "H" (HelloSpec.scala:5)
[info] Run completed in 135 milliseconds.
[info] Total number of tests run: 1
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 0, failed 1, canceled 0, ignored 0, pending 0
[info] *** 1 TEST FAILED ***
[error] Failed tests:
[error]   HelloSpec
[error] (Test / testQuick) sbt.TestsFailedException: Tests unsuccessful
```

## 테스트 통과 케이스 만들기
- 에디터로 `src/test/scala/HelloSpec.scala`를 다음과 같이 수정한다:
```scala
import org.scalatest.funsuite._

class HelloSpec extends AnyFunSuite {
  test("Hello should start with H") {
    // Hello, as opposed to hello
    assert("Hello".startsWith("H"))
  }
}
```
- 테스트가 통과한 것을 확인하고 **Enter**를 눌러 테스트를 종료한다.



## 라이브러리 의존성 추가하기
- 에디터를 이용해 `build.sbt`를 아래와 같이 수정한다:
```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

lazy val hello = (project in file("."))
  .settings(
    name := "Hello",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  )
```
- 수정사항을 반영하기 위해 `reload` 명령어를 실행한다.


## Scala REPL 이용하기
- Scala REPL은 간단하게 커맨드 라인에서 스칼라를 쳐보고 테스트 하기 위한 스칼라 쉘이다.
- REPL은 Read, Evaluate, Print, Loop의 줄임말
- `:paste` 명령어는 스칼라 콘솔에서 테스트하고 싶은 예제 코드가 있을 때 사용
- 실행 라인이 아닌 전체 예제 코드를 실행하고 싶을 때 :paste 사용

- 우리는 뉴욕의 현재 날씨를 확인할 수 있다.
    - 예제의 https://www.metaweather.com 사이트는 현재 폐쇄되어 이용할 수 없음.
    - 따라서 아래 내용을 실행 시 에러가 나는 것이 정상
```shell
sbt:Hello> console
[info] Starting scala interpreter...
Welcome to Scala 2.12.7 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_171).
Type in expressions for evaluation. Or try :help.

scala> :paste
// Entering paste mode (ctrl-D to finish)

import scala.concurrent._, duration._
import gigahorse._, support.okhttp.Gigahorse
import play.api.libs.json._

Gigahorse.withHttp(Gigahorse.config) { http =>
  val baseUrl = "https://www.metaweather.com/api/location"
  val rLoc = Gigahorse.url(baseUrl + "/search/").get.
    addQueryString("query" -> "New York")
  val fLoc = http.run(rLoc, Gigahorse.asString)
  val loc = Await.result(fLoc, 10.seconds)
  val woeid = (Json.parse(loc) \ 0 \ "woeid").get
  val rWeather = Gigahorse.url(baseUrl + s"/$woeid/").get
  val fWeather = http.run(rWeather, Gigahorse.asString)
  val weather = Await.result(fWeather, 10.seconds)
  ({Json.parse(_: String)} andThen Json.prettyPrint)(weather)
}

// press Ctrl+D

// Exiting paste mode, now interpreting.

import scala.concurrent._
import duration._
import gigahorse._
import support.okhttp.Gigahorse
import play.api.libs.json._
res0: String =
{
  "consolidated_weather" : [ {
    "id" : 6446939314847744,
    "weather_state_name" : "Light Rain",
    "weather_state_abbr" : "lr",
    "wind_direction_compass" : "WNW",
    "created" : "2019-02-21T04:39:47.747805Z",
    "applicable_date" : "2019-02-21",
    "min_temp" : 0.48000000000000004,
    "max_temp" : 7.84,
    "the_temp" : 2.1700000000000004,
    "wind_speed" : 5.996333145703094,
    "wind_direction" : 293.12257757287307,
    "air_pressure" : 1033.115,
    "humidity" : 77,
    "visibility" : 14.890539250775472,
    "predictability" : 75
  }, {
    "id" : 5806299509948416,
    "weather_state_name" : "Heavy Cloud",
...

scala> :q // to quit

```

## 서브 프로젝트 생성
- `build.sbt` 를 아래와 같이 수정한다:
```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

lazy val hello = (project in file("."))
  .settings(
    name := "Hello",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
  )
```
- 수정사항을 반영하기 위해 `reload` 명령어를 실행한다.



## 모든 서브 프로젝트 목록 출력
```shell
sbt:Hello> projects
[info] In file:/tmp/foo-build/
[info]   * hello
[info]     helloCore
```

## 서브 프로젝트 컴파일
```shell
sbt:Hello> helloCore/compile
```

## 서브 프로젝트에 ScalaTest 추가하기
- `build.sbt` 수정하기:
```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7"

lazy val hello = (project in file("."))
  .settings(
    name := "Hello",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
    libraryDependencies += scalaTest % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies += scalaTest % Test,
  )
```

## 명령어 중계
- sbt에서 말하는 aggregate란 집계된(aggregated) 프로젝트 중 하나에서 어떤 작업이 수행되면 함께 집계된 다른 프로젝트들에서도 수행된다는 개념이다.
- https://www.scala-sbt.org/1.x/docs/Multi-Project.html 참고
- aggregate 를 세팅하여 hello로 보내진 명령어가 helloCore에도 전달되도록 설정:
```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7"

lazy val hello = (project in file("."))
  .aggregate(helloCore)
  .settings(
    name := "Hello",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
    libraryDependencies += scalaTest % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies += scalaTest % Test,
  )
```
- `reload`, `~testQuick`를 실행한 후 두 서브프로젝트를 모두 실행해본다


## hello가 helloCore를 의존하도록 만들어보기
- `.dependsOn(...)`를 이용해서 다른 서브프로젝트에 대한 의존성을 추가한다.
- 또한 Gigahorse에 대한 의존성을 `helloCore`에 만든다.

```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7"

lazy val hello = (project in file("."))
  .aggregate(helloCore)
  .dependsOn(helloCore)
  .settings(
    name := "Hello",
    libraryDependencies += scalaTest % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies += "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0",
    libraryDependencies += scalaTest % Test,
  )
```


## Play JSON으로 JSON 파싱하기
- `helloCore`에 PLAY JSON을 추가한다.
```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7"
val gigahorse = "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0"
val playJson  = "com.typesafe.play" %% "play-json" % "2.9.2"

lazy val hello = (project in file("."))
  .aggregate(helloCore)
  .dependsOn(helloCore)
  .settings(
    name := "Hello",
    libraryDependencies += scalaTest % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies ++= Seq(gigahorse, playJson),
    libraryDependencies += scalaTest % Test,
  )
```
- `reload` 뒤에 `core/src/main/scala/example/core/Weather.scala`에 다음 내용을 추가한다:
```scala
package example.core

import gigahorse._, support.okhttp.Gigahorse
import scala.concurrent._, duration._
import play.api.libs.json._

object Weather {
  lazy val http = Gigahorse.http(Gigahorse.config)

  def weather: Future[String] = {
    val baseUrl = "https://www.metaweather.com/api/location"
    val locUrl = baseUrl + "/search/"
    val weatherUrl = baseUrl + "/%s/"
    val rLoc = Gigahorse.url(locUrl).get.
      addQueryString("query" -> "New York")
    import ExecutionContext.Implicits.global
    for {
      loc <- http.run(rLoc, parse)
      woeid = (loc \ 0  \ "woeid").get
      rWeather = Gigahorse.url(weatherUrl format woeid).get
      weather <- http.run(rWeather, parse)
    } yield (weather \\ "weather_state_name")(0).as[String].toLowerCase
  }

  private def parse = Gigahorse.asString andThen Json.parse
}
```

- 다음으로, `src/main/scala/example/Hello.scala`를 다음과 같이 수정한다:
```scala
package example

import scala.concurrent._, duration._
import core.Weather

object Hello {
  def main(args: Array[String]): Unit = {
    val w = Await.result(Weather.weather, 10.seconds)
    println(s"Hello! The weather in New York is $w.")
    Weather.http.close()
  }
}
```

- app을 실행하고 성공적으로 작동하는지 확인한다
    - 위에서 말했듯 예시 속 사이트가 현재 폐쇄되어 실패해야 정상
```shell
sbt:Hello> run
[info] Compiling 1 Scala source to /tmp/foo-build/core/target/scala-2.12/classes ...
[info] Done compiling.
[info] Compiling 1 Scala source to /tmp/foo-build/target/scala-2.12/classes ...
[info] Packaging /tmp/foo-build/core/target/scala-2.12/hello-core_2.12-0.1.0-SNAPSHOT.jar ...
[info] Done packaging.
[info] Done compiling.
[info] Packaging /tmp/foo-build/target/scala-2.12/hello_2.12-0.1.0-SNAPSHOT.jar ...
[info] Done packaging.
[info] Running example.Hello
Hello! The weather in New York is mostly cloudy.
```


## sbt-native-packager 플러그인 추가하기
- 에디터를 이용해 `project/plugins.sbt`를 생성하고 아래 내용을 추가한다:
```sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.4")
```

- 다음으로 `build.sbt`를 수정하고 `JavaAppPackaging:` 을 추가한다:
```sbt
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7"
val gigahorse = "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0"
val playJson  = "com.typesafe.play" %% "play-json" % "2.9.2"

lazy val hello = (project in file("."))
  .aggregate(helloCore)
  .dependsOn(helloCore)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "Hello",
    libraryDependencies += scalaTest % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies ++= Seq(gigahorse, playJson),
    libraryDependencies += scalaTest % Test,
  )
```

## 리로드 한 뒤 .zip 형식의 빌드산출물 생성하기
```shell
sbt:Hello> reload
...
sbt:Hello> dist
[info] Wrote /tmp/foo-build/target/scala-2.12/hello_2.12-0.1.0-SNAPSHOT.pom
[info] Wrote /tmp/foo-build/core/target/scala-2.12/hello-core_2.12-0.1.0-SNAPSHOT.pom
[info] Your package is ready in /tmp/foo-build/target/universal/hello-0.1.0-SNAPSHOT.zip
```

- 
```shell
$ cd /tmp/someother
$ unzip -o -d /tmp/someother /tmp/foo-build/target/universal/hello-0.1.0-SNAPSHOT.zip
$ ./hello-0.1.0-SNAPSHOT/bin/hello
Hello! The weather in New York is mostly cloudy.
```


## 앱을 Dockerize하기
- Dockerizing이란 도커 컨테이너를 이용하여 어플리케이션을 패킹, 디플로잉, 러닝하는 과정을 말한다.
```shell
sbt:Hello> Docker/publishLocal
....
[info] Successfully built b6ce1b6ab2c0
[info] Successfully tagged hello:0.1.0-SNAPSHOT
[info] Built image hello:0.1.0-SNAPSHOT
```
- Dockerized app:
```shell
$ docker run hello:0.1.0-SNAPSHOT
Hello! The weather in New York is mostly cloudy
```

## 버전 세팅하기
```sbt
ThisBuild / version      := "0.1.0"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / organization := "com.example"

val scalaTest = "org.scalatest" %% "scalatest" % "3.2.7"
val gigahorse = "com.eed3si9n" %% "gigahorse-okhttp" % "0.5.0"
val playJson  = "com.typesafe.play" %% "play-json" % "2.9.2"

lazy val hello = (project in file("."))
  .aggregate(helloCore)
  .dependsOn(helloCore)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "Hello",
    libraryDependencies += scalaTest % Test,
  )

lazy val helloCore = (project in file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies ++= Seq(gigahorse, playJson),
    libraryDependencies += scalaTest % Test,
  )
```

## 스칼라 버전 임시로 변경하기
- 프로젝트 디렉토리에서 `++2.12.14!'와 같이 버전명 입력
```shell
sbt:Hello> ++2.12.14!
[info] Forcing Scala version to 2.12.14 on all projects.
[info] Reapplying settings...
[info] Set current project to Hello (in build file:/tmp/foo-build/)
```
- `scalaVersion` 세팅을 확인한다:
```shell
sbt:Hello> scalaVersion
[info] helloCore / scalaVersion
[info]  2.12.14
[info] scalaVersion
[info]  2.12.14
```
- 이 세팅은 `reload` 후에는 사라진다.

## dist task 검출하기
- `dist`에 관련된 내용을 좀 더 알아보기 위해서는 `help`와 `inspect` 이용
```sbt

```
