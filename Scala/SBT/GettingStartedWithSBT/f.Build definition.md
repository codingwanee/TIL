# Build definition
- https://www.scala-sbt.org/1.x/docs/Basic-Def.html 에 대한 번역 및 정리
- 정식 번역이 아니므로 의역 및 오역, 재정리 된 내용이 있음

## 도입부
- 본 내용은 sbt를 설치했다는 가정 하에 작성되었다.
- sbt가 어떻게, 왜 동작하는지에 앞서 예시를 보며 시작해보도록 하자.
- 현 페이지는 `build.sbt` 의 빌드 정의에 대한 내용을 담는다.


## sbt version 명시하기
- 빌드의 버전을 명시하는 것 또한 build definition에 포함된다.
- 버전을 명시함으로써 sbt launcher가 동일한 프로젝트에서 각기 다른 버전으로 견고한 산출물을 낼 수 있도록 한다.
- `project/build.properties`라는 이름의 파일을 생성해서 아래와 같이 sbt version을 써준다
```conf
sbt.version=1.7.2
```

- 만일 명시된 버전을 로컬에서 지원할 수 없는 경우, `sbt launcher`는 명시된 버전을 다운로드해 준다.
- 파일이 존재하지 않으면 sbt launcher는 임의의 버전으로 대신 선택한다. 그러나 이 경우 빌드를 이식할 수 없게 되기 때문에 권장되지 않는다.

## build definition이란?
- `build definition`은 `build.sbt` 안에 정의되어 있으며, projects(Project 타입)로 구성된다.
- *프로젝트*라는 용어가 애매할 수 있으므로 이 가이드에서는 종종 `서브프로젝트 subproject` 라고 쓰려고 한다.

- 예를 들어, `build.sbt` 안에는 아래와 같이 현재 디렉토리의 서브프로젝트를 명시한다:

```conf
lazy val root = (project in file("."))
  .settings(
    name := "Hello",
    scalaVersion := "2.12.7"
  )
```

- 각 서브프로젝트는 key-value 형태로 이루어져 있다.
- 예를 들어 `name`이라는 key를 하나 보면 서브프로젝트의 이름을 나타내는 string 값과 매핑되어 있다.
- key-value 쌍은 아래와 같이 `.settings(...)` 메소드에 나열되어 있다.

```conf
lazy val root = (project in file("."))
  .settings(
    name := "Hello",
    scalaVersion := "2.12.7"
  )
```


## build.sbt가 세팅을 명시하는 법
- `build.sbt`는 build.sbt domain-specific-languages(DSL)를 이용해 

```conf
ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.12.16"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "hello"
  )
```

- `build.sbt` DSL을 좀 더 자세히 살펴보자:

```conf
organization := { "com.example" }

organization        # key
:=                  # operator
{ "com.example" }   # (setting/task) body
```

- 각 entry는 setting expression이라고 부른다.
- 이 중 일부는 task expression이라고 불리는데, 이 둘의 차이점은 이 페이지의 나중에 살펴보도록 하자.

- setting expression은 아래 세 가지 요소를 포함한다.
    1. key, 왼쪽에 위치한 것.
    2. 연산자, 여기에서는 :=
    3. body 또는 setting body, 오른쪽에 위치한 것

- 왼쪽에 오는 `name`, `version`, `scalaVersion`은 key라고 불린다.
- key는 SettingKey[T], TaskKey[T], InputKey[T] 의 인스턴스이다. `T`는 value의 타입이 오는 자리다.
- 각 key의 유형에 대한 설명은 아래에 있다.

- `name` 키는 `SettingKey[String]`의 유형이고, name 키에 붙는 `:=` 연산자는 따로 `String` 타입으로 규정되어 있다.
- 다른 유형의 값을 넣으면 build definition이 컴파일되지 않는다.

```conf
lazy val root = (project in file("."))
  .settings(
    name := 42  // will not compile
  )
```

- `build.sbt`는 `val`과 `lazy val`, `def`로 산재되어 있다.
- 최상위 `object`와 `class`는 `build.sbt`에 포함시킬 수 없다.
- 이 둘은 `projects/` 디렉토리 아래 스칼라 소스 파일로 들어가야 한다.


## Keys

#### Types
- key에는 3개의 유형이 있다
    - SettingKey[T] : 한 번만 평가되는 값에 대한 key (value는 서브프로젝트가 로딩될 때 산정되고 계속 유지된다.)
    - TeskKey[T] : task라고 불리는 값에 대한 key. 참조될 때마다 평가되며,(scala function과 유사)잠재적으로 부작용이 있다.
    - InputKey[T] : command line arguments를 가진 task에 대한 key

#### Built-in Keys
- built-in 키는 `Keys`라고 불리는 객체의 필드이다.
- `build.sbt`는 암묵적으로 `import sbt.Keys._`를 갖는다. 따라서 `sbt.Keys.name`은 `name`으로 참조될 수 있다.

#### Custom Keys
- Custom key는 각각의 생성 메소드로 정의할 수 있다: `settingKey`, `taskKey`, `inputKey`
- Each method expects the type of the value associated with the key as well as a description.
- key의 이름은 key가 할당된 `val`로부터 가져온다.
- 예를 들어, 아래에서 `hello`라는 task의 키를 정의하기 위해선 아래와 같이 쓸 수 있다
```conf
lazy val hello = taskKey[Unit]("An example task")
```
