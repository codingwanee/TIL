# Getting Started with sbt
- https://www.scala-sbt.org/1.x/docs/Getting-Started.html 에 대한 번역 및 정리
- 정식 번역이 아니므로 의역 및 오역, 재정리 된 내용이 있음

## 도입부
- sbt는 간단한 몇 가지 컨셉으로 유연하고 강력한 build definition 지원이 가능하다.
- 컨셉은 간단하지만, doc을 읽지 않으면 꽤 헤맬 수 있다.
- Getting Started Guilde는 sbt build definition을 생성하고 유지하기 위한 컨셉들에 대한 설명을 담고 있다. (따라서 읽어보기를 강력히 추천한다!)
- 하지만 아주 급한 상태라면, build definition, scopes, task graph에 대한 글을 먼저 읽어도 좋다. (그렇지만 이 문서를 먼저 읽는것이 좋을것이다)
- Enjoy!


## sbt 설치하기
- sbt project를 생성하기 위해서 아래 단계를 밟는다
    - JDK 설치
    - sbt 설치
    - [hello world](https://www.scala-sbt.org/1.x/docs/Hello.html) 간단한 프로젝트 세팅
    - [running](https://www.scala-sbt.org/1.x/docs/Running.html)으로 넘어가서 sbt 실행에 대해 알아보기
    - [.sbt build definition](https://www.scala-sbt.org/1.x/docs/Basic-Def.html) 으로 넘어가서 build definition에 대해 좀더 깊게 알아보기
- 궁극적으로 sbt를 설치하는 것은 결국 JAR 런처와 shell script로 귀결된다. 하지만 사용자의 플랫폼에 따라, 좀 덜 지루한 몇가지 방법을 제공한다.
    - [macOS](##macOS-맥에서-sbt-설치하기)
    - [Windows](##Windows-윈도우에-sbt-설치하기)
    - [Linux](##Linux-리눅스에-sbt-설치하기)

## Tips and Notes
- sbt 실행에 어려움이 있다면 terminal encodings, HTTP proxies, JVM options에 관한 [Setup Notes](https://www.scala-sbt.org/1.x/docs/Setup-Notes.html)를 참고한다.


## macOS 맥에서 sbt 설치하기

#### cs setup으로 sbt 설치하기
- [Install](https://www.scala-lang.org/download/?_ga=2.140976946.1466139420.1660201323-324890170.1660201323) 사이트를 참고해서 Coursier를 사용하는 Scala 설치
- sbt의 최신 안정 버전을 설치할 수 있는 방법이다.

#### JDK 설치하기
- [JDK 8 or 11](https://adoptium.net) 버전을 받거나 [SDKMAN!](https://sdkman.io)을 사용

#### SDKMAN!
```shell
$ sdk install java $(sdk list java | grep -o "\b8\.[0-9]*\.[0-9]*\-tem" | head -1)
$ sdk install sbt
```


#### universal package로부터 설치하기
- [ZIP](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.zip) 또는 [TGZ](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.tgz) 패키지 다운로드 후 압축 풀기


#### third-party package로부터 설치하기
- (참고) 써드파티 패키지는 최신버전을 제공하지 않을 수 있다. 만약 그로 인해 어떤 이슈가 생긴다면 관련된 유지보수자에게 보고하도록 하자

#### Homebrew
```shell
$ brew install sbt
```



## Windows 윈도우에 sbt 설치하기

#### cs setup으로 sbt 설치하기
- [Install](https://www.scala-lang.org/download/?_ga=2.189081387.1466139420.1660201323-324890170.1660201323) 사이트를 참고해서 Coursier를 사용하는 Scala 설치
- sbt의 최신 안정 버전을 설치할 수 있는 방법이다.

#### JDK 설치하기
- [JDK 8 or 11](https://adoptium.net) 버전 다운로드

#### universal package로부터 설치하기
- [ZIP](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.zip) 또는 [TGZ](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.tgz) 패키지 다운로드 후 압축 풀기

#### Windows installer
- [msi installer](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.msi) 다운로드 및 설치

#### third-party package로부터 설치하기
- (참고) 써드파티 패키지는 최신버전을 제공하지 않을 수 있다. 만약 그로 인해 어떤 이슈가 생긴다면 관련된 유지보수자에게 보고하도록 하자

#### Scoop
```shell
$ scoop install sbt
```

#### Chocolatey
```shell
$ choco install sbt
```


## Linux 리눅스에 sbt 설치하기

#### cs setup으로 sbt 설치하기
- [Install](https://www.scala-lang.org/download/?_ga=2.189081387.1466139420.1660201323-324890170.1660201323) 사이트를 참고해서 Coursier를 사용하는 Scala 설치
- sbt의 최신 안정 버전을 설치할 수 있는 방법이다.

#### JDK 설치하기
- [JDK 8 or 11](https://adoptium.net) 버전 다운로드

#### universal package로부터 설치하기
- [ZIP](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.zip) 또는 [TGZ](https://github.com/sbt/sbt/releases/download/v1.7.1/sbt-1.7.1.tgz) 패키지 다운로드 후 압축 풀기

#### Ubuntu and other Debian-based distributions
- sbt는 [DEB](https://dl.bintray.com/sbt/debian/sbt-1.7.1.deb) 패키지를 공식적으로 지원한다.
- ubuntu와 다른 Debian 기반의 빌드산출물은 DEB 형식을 사용하지만, 보통 사용자들은 로컬에서 DEB 파일로 소프트웨어 설치를 하지는 않는다.
- 대신 CLI로 package 매니저를 이용하거나(e.g. apt-get, aptitude) GUI를 이용한다.(e.g.)
- 터미널에서 아래 명령어를 실행하여 sbt 설치

```shell
sudo apt-get update
sudo apt-get install apt-transport-https curl gnupg -yqq
echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list
curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo -H gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import
sudo chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg
sudo apt-get update
sudo apt-get install sbt
```
- 패키지 매니저는 설치를 위해 수많은 configured repository들을 확인한다. 사용자는 패키지 매니저가 확인할 repository를 추가하기만 하면 된다.
- 한 번 sbt가 설치되고 나면 aptitude 또는 시냅스에서 패키지 캐시를 업데이트 한 후부터 패키지를 관리할 수 있게 된다.
- System Settings -> Software & Updates -> Other Software: 의 하단에서 추가된 레포지토리를 확인할 수 있다.

- *Note*
    - 기존에 Ubuntu에서 SSL 에러가 발생한다는 몇몇 리포트가 있었다:
    ```log
    Server access Error: java.lang.RuntimeException: Unexpected error: java.security.InvalidAlgorithmParameterException: the trustAnchors parameter must be non-empty url=https://repo1.maven.org/maven2/org/scala-sbt/sbt/1.1.0/sbt-1.1.0.pom
    ```
    - 명백하게도 이 에러는 PKCS12 포맷을 사용하는 OpenJDK 9 의 /etc/ssl/certs/java/cacerts [cert-bug](https://bugs.launchpad.net/ubuntu/+source/ca-certificates-java/+bug/1739631)에서 파생된 것으로 보인다. https://stackoverflow.com/a/50103533/382 에 따르면 해당 이슈는  Ubuntu Cosmic (18.10)에서는 해결된 것으로 보이나, Ubuntu Bionic LTS (18.04)에서는 아직 release를 기다리고 있다.
- *Note*
    - [sudo apt-key adv --keyserver hkps://keyserver.ubuntu.com:443 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823] 는 buggy GnuPG를 이용하기 때문에 Ubuntu Bionic LTS (18.04)에서는 동작하지 않을 수 있다. 따라서 이 경우 web API를 이용해 위 public key를 다운로드 받기를 권장한다.


#### Red Hat Enterprise Linux and other RPM-based distributions 
- sbt는 공식적으로 [RPM](https://dl.bintray.com/sbt/rpm/sbt-1.7.1.rpm) 패키지를 지원한다.
- Red Hat Enterprise Linux는 RPM 형식의 다른 RPM 기반의 빌드산출물을 사용한다.
- 터미널에서 다음 명령어를 실행해서 sbt를 설치한다. (sudo를 써야 하므로 supseruser 권한 필요)
```shell
# remove old Bintray repo file
sudo rm -f /etc/yum.repos.d/bintray-rpm.repo
curl -L https://www.scala-sbt.org/sbt-rpm.repo > sbt-rpm.repo
sudo mv sbt-rpm.repo /etc/yum.repos.d/
sudo yum install sbt
```

- Fedora 31 이상에서는 sbt-rpm.repo로 다음을 실행:
```shell
# remove old Bintray repo file
sudo rm -f /etc/yum.repos.d/bintray-rpm.repo
curl -L https://www.scala-sbt.org/sbt-rpm.repo > sbt-rpm.repo
sudo mv sbt-rpm.repo /etc/yum.repos.d/
sudo dnf install sbt
```

- *Note*
    - 위와 관련된 이슈가 발견될 경우 [sbt](https://github.com/sbt/sbt) 프로젝트로 리포트 해주세용


#### Gentoo
- 오피셜 트리는 가장 최신 버전을 다운로드 하기 위해서 아래 명령어 실행
```shell
$ emerge dev-java/sbt
```
