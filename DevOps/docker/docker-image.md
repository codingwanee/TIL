# 도커 이미지 Docker Image
- docker image란 container를 실행시키기 위한 설계도라고 볼 수 있다.
- 도커 이미지가 있어야 컨테이너를 생성 및 실행할 수 있다.


## 순서
1. Dockerfile 작성
2. Docker 이미지 빌드
3. Docker 이미지 실행


## Dockerfile 작성하기
- Dockerfile이란, Docker 이미지를 생성하기 위한 빌드 지시사항들을 포함한 스크립트
- Dockerfile을 작성하면서 이미지의 기본 설정, 응용 프로그램 설치, 환경 변수, 파일 및 폴더 생성 등을 저장할 수 있다.

#### 레이어 시스템
- 도커파일을 만들 때 레이어 시스템이라는 시스템을 사용하는데 아래 작성한 내용을 명령어 당 한줄씩 한 레이어라고 한다.
- 

#### Dockerfile 구성
1. 베이스 이미지
- FROM: 생성할 이미지의 베이스가 되는 이미지
    ```yaml
    # 이미 구현되어 있는 이미지 명시
    # FROM {baseImage명}:{version}
    FROM ubuntu:18.04
    ```
- Docker Hub에서 공식 이미지를 찾거나 자신이 작성한 이미지를 사용할 수 있다.

2. dockerfile  명령어
- RUN: 새로운 레이어에서 명령어를 실행
    ```yaml
    # 3rd party apk 설치
    RUN apk add --update bash
    RUN mkdir -p /data/log/dtag/supervisord
    ```
- CMD: 컨테이너가 시작되었을 때 바로 실행할 명령어 지정
    ```yaml
    ```
- ENTRYPOINT: 컨테이너가 시작되었을 때 스크립트 실행
- COPY/ADD: 파일이나 디렉토리를 이미지 내부로 복사
    - COPY와 ADD의 다른점은 ADD는 압축파일은 해제 뒤에 추가, wget 명령어 이용하여 추가도 가능
    ```yaml
    # PKG
    # COPY {호스트에서 가져올 파일이나 디렉토리 경로} {컨테이너 안에 경로}
    # ADD {호스트에서 가져올 파일이나 디렉토리 경로} {컨테이너 안에 경로}
    COPY src/docker/bin/start-digd.sh /data/work/dtag/digd/bin/start-digd.sh
    ADD ./target/universal/digd-*.zip /data/app/dtag/
    ```
- ENV: 환경 변수를 설정, runtime variable
    - 환경변수란, 보안정보나 서버정보와 같이 런타임에 사용하는 값을 저장
    - 운영체제 또는 컨테이너 전체에서 사용 가능하며, 런타임에 값을 변경 가능하다.
    ```yaml
    # ENV is runtime variables
    # ENV {환경 변수명}={변수}
    ENV DIGD_SERVER_PORT=$ARG_DIGD_SERVER_PORT
    ENV ZK_ADDR=$ARG_ZK_ADDR
    ```
- ARG: 컴파일 변수를 설정, compile time variable (default value)
    - 컴파일 변수란, 코드를 컴파일할 때 사용하는 값을 저장하는 변수
    - 소스코드 내에서만 사용 가능하며, 코드를 컴파일할 때 값을 할당하기 때문에 값을 변경할 수 없다.
    ```yaml
    # ARG is build time variables (default value)
    ARG ARG_DIGD_SERVER_PORT=8023
    ARG ARG_ZK_ADDR="zookeeper:2181"
    ```
- EXPOSE: 컨테이너가 사용하는 포트를 지정(보통 명령어 run에서 -p로 대체)
    ```yaml
    ```
- WORKDIR: 생성된 컨테이너 안에서 명령어가 실행될 디렉토리 지정
    ```yaml
    # WORKDIR ${FOO}
    # WORKDIR {컨테이너 안에서 사용할 경로}
    WORKDIR /home/app
    ```

## Docker 이미지 빌드하기
```shell
# dockerfile 빌드하기 
docker build -t {이미지명} {작성한 도커파일 경로}
docker build -t frontend   .
# 해당 디렉토리에 이동한후 Dockerfile이라고 생성하고 build를 하면 dockerfile명을 적지 않고 
# .으로 적어도 무방하다.

# dockerfile 로 빌드한 이미지 실행 
docker run --name {실행image명} -v $(pwd):{workdir} -p {입력포트}:{컨테이너내부포트} {컨테이너명}
docker run --name front         -v $(pwd):/home/app -p 8080:8080  front-container
```



## Docker 이미지 실행하기
```shell
# image 내려받기 
# docker pull {imageName}:{tag} - tag는 생략하면 latest로 자동 적용
docker pull tomcat:latest

# image를 container로 실행하기 기본명령어 
# docker run {OPTION} {imageName}:{tag} {command} 
docker run -it tomcat:latest /bin/bash

# container에 이름을 할당하여 실행하기
# docker run {OPTION} --name {containerName} {imageName}:{tag} {command} 
docker run -it --name tomcatserver tomcat:latest /bin/bash

# 백그라운드로 실행하기 
# docker run {OPTION} -d --name {containerName} {imageName}:{tag}
# 백그라운드에서 실행 하니 command를 정하지 않아도 된다.
docker run -it -d --name tomcatserver tomcat:latest 

# 포트포워딩
# docker run {OPTION} --name {containerName} -p {외부접속포트}:{내부연동포트} {imageName}:{tag} {command}
# 아래와 같이 설정하면 9999포트로 들어온것을 8080포트와 매칭해준다. 
docker run -it --name tomcatserver -p 9999:8080 tomcat:latest /bin/bash

# 환경 설정 추가하여 실행하기
# docker run {OPTION} --name {containerName} {imageName}:{tag} -e {ENV변수명}={value}
docker run -d -p 9999:8080 --name mysql-container -e TOMCAT_ID=tomcat

# 실제 내가 파일을 관리하고 싶을 때 마운트 시켜 실행 시키기
# 마운트를 안시키면 내부적으로 생성된 파일은 모두 사라지기 때문에 마운트 시키는 법은 꼭 알아두자.
docker run -d --name {containerName} -v {local 마운트 위치}:{container내부 마운트 위치} {imageName}:{tag}
docker run -d --name mysql-container -v c:/users/path:/home/tomcat

# container 종료와 함께 사라지게 하기 
# docker run -it -rm {containerName}
docker run -it -rm tomcatserver


# ---------------------------------------------------------------------
# continer 목록 확인 
# 실행 되어있는 것 
docker ps 

# 종료 되어있는것 모두 
docker ps -a


# ---------------------------------------------------------------------
# run해서 container가 만들어진 상황이라면 아래의 container 동작 명령어 사용가능 
# container 시작
docker start {containerName}

# container 종료 
docker stop {containerName}

# container 재시작
docker restart {containerName}

# container 삭제 - container 종료 후에 실행 
docker rm {containerName}

# container 삭제 - 강제 삭제
docker rm -f {containerName}

# 실행 된 docker container 접속하기
# container 실행시 사용한다. 
docker attach {containerName}

# 외부에서 컨테이너 진입할 때 사용한다.
docker exec -it [container_name] /bin/bash


# ----------------------------------------------------------------------
# command로 실행 했을 때 Container terminal에서 빠져나오기 
exit
ctrl + c
```

