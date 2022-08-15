# 실행하기



## sbt shell
- 당신의 프로젝트 경로에서 다른 인자 없이 sbt를 실행한다:
```shell
$ sbt
```

- 어떤 명령어 없이 sbt를 실행하면 sbt shell을 시작한다. sbt shell은 명령 프롬프트를 갖는다.(tab 자동완성과 history도!)
- 예를 들어, 당신은 sbt shell에 `compile`이라고 칠 수 있다.
```shell
> compile
```
- 다시 `compile`을 실행하기 위해서 위쪽 화살표를 누르고 엔터를 입력하면 된다.
- 프로그램을 실행하고 싶다면, `run`을 입력한다.
- sbt shell을 종료하고 싶다면, `exit`을 입력하거나 Ctrl+D(Unix) 또는 Ctrl+Z(Windows)를 이용한다.

## 배치 모드
- 당신은 또한 sbt를 배치 모드에서 이용할 수도 있다.
- 공백으로 sbt 명령어와 인자들을 구분한다.
- 인자가 있는 sbt 명령어를 이용할 때는 명령어와 인자들을 따옴표로 묶어 하나의 인자처럼 보낼 수 있다:
```shell
sbt clean compile "testOnly TestA TestB"
```
- 위 예시에서 `testOnly`는 `TestA`와 `TestB`를 갖는다.
- 명령어는 clean, compile, 그리고 testOnly 순으로 실행된다.

- *Note* 배치모드는 실행할 때마다 JVM 스핀업과 JIT을 사용한다. 따라서 빌드 속도가 훨씬 느리게 돌아갈 것이다. 매일매일 진행되는 코딩에서는 sbt shell을 이용하거나 아래에서 설명하는 지속적 빌드와 테스트 기능을 이용하기를 권장한다.

- sbt 0.13.16부터, sbt에서 배치모드를 이용하게 되면 시작할 때 정보성 메시지가 뜬다.
```shell
$ sbt clean compile
[info] Executing in batch mode. For better performance use sbt's shell
...
```
- 이것은 `sbt compile`을 할 때만 뜨는 메시지이며, `suppressSbtShellNotification := true` 설정으로 뜨지 않도록 할 수 있다.


## 지속적 빌드와 테스트
- 수정-컴파일-테스트 작업 싸이클에 속도를 붙이기 위해, 당신은 소스가 수정될 때마다 sbt에게 자동으로 재컴파일 후 실행하는 테스트를 수행하도록 맡길 수 있다.
- `~` 접두어를 이용한 명령어로 소스가 한 군데 이상 수정된 경우에 실행되도록 하자. 예를 들어, sbt shell에서 다음을 시도해보자:
```shell
> ~testQuick
```
- enter를 눌러 중지한 뒤에 변화를 살펴보자.
- `~` 접두어는 sbt shell과 배치모드 둘 다에서 사용할 수 있다.
- [Triggered Execution](https://www.scala-sbt.org/1.x/docs/Triggered-Execution.html)에서 좀 더 상세한 내용을 알 수 있다.


## 자주 쓰는 명령어
- 여기 가장 자주 쓰이는 sbt 명령어들을 모아보았다.
- 좀 더 다양한 명령어들은 [Command Line Reference](https://www.scala-sbt.org/1.x/docs/Command-Line-Reference.html)에서 확인할 수 있다.


| command | Description|
|:---:|:---|
|clean | target 디렉토리에 생성된 파일을 모두 삭제 |
|compile | 메인 소스를 컴파일 (src/main/scala 와 src/main/java 디렉토리)
| test | 모든 테스트를 컴파일 및 실행 |
| console | classpath에서 컴파일 된 소스와 의존성들을 포함하여 scala 인터프리터를 시작. sbt로 돌아가기 위해서는 다음을 입력: quit, Ctrl+D(Unix), Ctrl+Z(Windows) |
| run <argument>*| sbt와 동일한 vm에 있는 프로젝트의 메인 클래스를 실행 |
| package | src/main/resources에 있는 파일들과 /src/main/scala 그리고 /src/main/java 의 컴파일 된 클래스를 포함한 jar 파일 생성 |
| help <command> | 특정 명령어에 대한 상세한 help 내용을 보여준다. 명령어를 특정하지 않으면 모든 명령어에 대한 간략한 설명을 보여준다. |
| reload | build definition을 리로드한다.(build.sbt, project/ 디렉토리의 *.scala, *.sbt 파일) build definition을 변경했을 때 필요한 명령어 |

## 탭 자동완성
- sbt shell은 탭 자동완성 기능을 갖고 있다.
- 아무것도 쓰지 않은 프롬프트에서도 사용 가능하다.
- sbt 컨벤션으로서, tab을 한 번만 누르면 자주 쓰이는 목록을, 여러 번 누르면 상세한 목록을 보여준다.

## sbt shell history
- sbt shell은 당신이 sbt를 종료하고 재시작하더라도 그 전의 history를 기억하고 있다.
- history로 접근하는 가장 쉬운 방법은 위쪽 화살표를 눌러 이전에 입력했던 명령어들을 찾아보는 것이다.
- *Note* `Ctrl-R`을 누르면 history 내역을 역방향으로 **검색**해준다.
- 터미널 환경에서 JLine's integration을 이용하면 `$HOME/ .inputrc` 파일을 수정해서 sbt shell을 커스터마이징 할 수 있다. 예를 들어 아래의 설정은 화살표를 위-아래로 움직일 때마다 접두어를 기반으로 history를 검색할 수 있게 한다.
    - (나의 추가설명)JLine은 콘솔 입력값을 다루는 Java 라이브러리이다. BSD editline, BNU readline과 기능적으로 유사하나 ZSH line editor 의 기능이 더해졌다는 특징이 있다.
```shell
"\e[A": history-search-backward
"\e[B": history-search-forward
"\e[C": forward-char
"\e[D": backward-char
```

- sbt shell은 또한 아래의 명령어들을 지원한다.

|command|description|
|:---|:---|
|!|history 명령어의 help를 보여준다|
|!!|이전 명령어를 다시 실행한다|
|!:|모든 이전 명령어를 보여준다(인덱스 포함)|
|!:n| 가장 최근 실행한 n개 명령어를 보여준다|
|!n|!: 명령어가 보여준 목록에서 n번째 인덱스를 실행|
|!-n|현재 입력하는 !-n 명령어 이전 n번째 명령어를 실행|
|!string|'string'으로 *시작*하는 가장 최근 명령어 실행|
|!?string|'string'을 *포함*하는 가장 최근 명령어 실행|
