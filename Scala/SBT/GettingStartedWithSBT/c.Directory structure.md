# 디렉토리 구조
- 본 페이지는 sbt를 설치 후 [sbt by example]()을 읽었다고 가정한다

## 기본 경로
- sbt에서 쓰는 용어 중에서 "기본 경로(base directory)"라는 말은 프로젝트를 포함하고 있는 디렉토리를 가리킨다.
- 만약 당신이 `/tmp/foo-build/build.sbt`를 포함하는 `hello`라는 프로젝트를 생성했다면 `/tmp/foo-build`가 베이스 디렉토리가 된다.

## 소스코드
- sbt는 **Maven**과 동일한 디렉토리 구조를 디폴트로 갖는다.(모든 경로들은 기본 경로의 상대 경로):
```config
src/
  main/
    resources/
       <files to include in main jar here>
    scala/
       <main Scala sources>
    scala-2.12/
       <main Scala 2.12 specific sources>
    java/
       <main Java sources>
  test/
    resources
       <files to include in test jar here>
    scala/
       <test Scala sources>
    scala-2.12/
       <test Scala 2.12 specific sources>
    java/
       <test Java sources>
```
- `src/` 뒤의 다른 경로들은 무시된다.
- 또한, 숨김처리 된 다른 경로들도 모두 무시된다.

- 소스 코드들은 프로젝트 기본 경로에서 `hello/app.scala` 에 위치할 수 있긴 하다. 그러나 작은 프로젝트에서는 괜찮더라도, 일반적인 경우라면 `src/main/` 디렉토리에 정갈하게 위치하는 게 보통이다. 당신이 `*.scala` 소스 코드가 기본 경로에 위치해도 된다는 사실은 괴상한 짓처럼 보이지만, 이 사실은 [나중에](https://www.scala-sbt.org/1.x/docs/Organizing-Build.html) 가면 상관있어진다.


## sbt 빌드 정의 파일
- 빌드에 대한 정의는 프로젝트 기본 경로의 `build.sbt`에 기술된다(실제로는 파일명에 관계 없이 `*.sbt`라는 이름이면 된다).


## 빌드 지원 파일
- build.sbt 외에도 프로젝트 경로에는 `.scala` 파일에 헬퍼 객체와 일회성 플러그인을 정의할 수도 있다. 지금은 배경지식을 먼저 쌓아야 하니 여기에 관한 내용은 [나중에](https://www.scala-sbt.org/1.x/docs/Organizing-Build.html) 다루게 될 것이다.


## 빌드 산출물
- 생성된 파일들(컴파일 된 클래스, jar 패키지, 관리 파일, 캐시, 도큐멘테이션)은 디폴트로 `target` 디렉토리에 위치한다.

## 버전 관리 구성하기 (형상 관리)
- 당신의 `.gitignore` (또는 유사한 다른 버전관리시스템)은 다음을 포함한다:
```shell
target/
```
- 뒤에 붙은 `/`는 의도적으로 붙은 것이다.
    - 경로임을 나타내기 위해
- 또한 앞에 `/`가 붙지 않은 것도 의도적이다.
    - 그냥 `target/`이라고 썼을 때 `project/target/`을 나타내기 위해
