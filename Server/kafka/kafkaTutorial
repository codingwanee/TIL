## 카프카 실습노트

1. Docker설치

   * 도커를 까는 이유는 도커에서 실행하면 이런저런 파일을 설치할 필요 없이 이미지만 띄워서 실습해볼 수 있기 때문이다.

2. Dockerfile 설치

   * Dockerfile: DockerImage를 생성하기 위한 스크립트(설정파일)
   * 배포를 용이하게 하고 컨테이너(이미지)가 특정 행동을 수행하도록 할 수 있다는 장점이 있다.
   * 인터넷에서 다른 누군가 설정해 놓은 도커파일을 clone으로 당겨와 손쉽게 설치 및 설정 가능
   * https://github.com/wurstmeister/kafka-docker
   * 원하는 로컬 위치로 이동해서 터미널에서 git clone https://github.com/wurstmeister/kafka-docker.git 실행

3. 컨테이너 실행 정의 작성파일 편집

   * docker-compose.yml : yaml으로 Docker 컨테이너에 관한 실행 옵션을 기재한 파일. 여러 개의 컨테이너로부터 이루어진 서비스를 구축, 실행하는 순서를 자동으로 하여 관리를 간단히 할 수 있다.
   * yaml으로 compose 파일 기재하는 방법 참고  https://docs.docker.com/compose/compose-file 
   * compose 파일을 준비하여 커맨드를 1회 실행하는 것으로 그 파일로부터 설정을 읽어들여 모든 컨테이너 서비스를 실행시키는 것이 가능하다.
   * 우리는 카프카를 하나만 띄울 것이기 때문에 "single" 키워드가 들어간 파일을 편집
     1. 터미널에서 vi docker-compose-single-broker.yml 실행
     2. KAFKA_ADVERTISED_HOST_NAME: 의 ip 부분을 localhost 로 변경 (127.0.0.1 도 같은 말)

4. Docker 실행

   * 터미널에서 docker-compose up -d 실행

   * 이 때, 빌드 실패가 뜰 것임. 방화벽 때문에 사이트가 제공하는 인증이 뭔가 안 맞아서 그러는 것

   * Taron이 가르쳐 준 아래 방법대로 해결

     * 해결방안 참고 https://stackoverflow.com/questions/66201209/docker-build-using-ca-trust-bundle-from-host

     ```text
         my solution. create "repositories" file. contents is :
     
         http://dl-cdn.alpinelinux.org/alpine/v3.13/main
         http://dl-cdn.alpinelinux.org/alpine/v3.13/community
         in Docker file, before "RUN apk update", add the followings:
     
         COPY repositories /etc/apk/repositories
     
         thanks.
     ```

     * 즉, Dockerfile에다가 COPY repositories /etc/apk/repositories 라인을 추가하라는 뜻
     * 터미널 창에 vi repositories 를 실행하면 쉽게 파일생성 가능

    * 다시 docker-compose up -d 실행하면 이번에는 정상적으로 실행됨   

5. Docker 확인

   * docker 화면을 열어보면 사이좋게 kafka, zookeeper가 사이좋게 하나씩 띄워진 것을 볼 수 있다.
   * zookeeper는 kafka와 짝꿍으로, 반드시 함께 다닌다.

6. 공식 사이트에서 제공하는 kafka 바이너리 파일 다운로드

   * https://kafka.apache.org/downloads
   * 원하는 버전을 찾아 tgz 파일 다운로드
   * 이 때, 위에서 작성한 Dockerfile과 버전정보가 일치하는 파일을 찾아야 한다는 점 주의!
   * 다음 명령어를 실행해서 파일 압축을 해제한다. tar -xvf kafka_2.13-2.8.1.tar
   * 이 파일들은 kafka를 컨트롤하기 위한 바이너리 파일로, 확장자가 sh 로 되어있다.

* 참고 https://miiingo.tistory.com/196
