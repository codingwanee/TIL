

# 도커 명령어

## docker pull, docker clone


## docker run

#### docker run 옵션
- -d
    - 컨테이너를 백그라운드에서 실행
- -it
    - -i 옵션과 -t 옵션을 함께 쓴 형태
    - 컨테이너를 종료하지 않고 터미널의 입력을 계속해서 컨테이너로 전달
    - 컨테이너의 쉘이나 CLI 도구를 이용할 때 유용하게 사용
- --name
    - 컨테이너를 식별하기 위해 이름을 부여해주는 옵션 (컨테이너ID 대신에 이름 사용)
    - CLI 등에서 컨테이너를 제어하기 훨씬 편리
- -e
    - 환경변수 지정
    - -e 옵션 사용시 Dockerfile에 ENV 설정을 덮어씀
- -p
    - host 컴퓨터에서 컨테이너에서 리스닝하고 있는 포트로 접속할 수 있도록 설정    
- -v
    - 호스트 컴퓨터의 파일 시스템의 특정 경로를 컨테이너의 파일 시스템의 특정 경로로 마운트
    - 호스트와 컨테이너 간의 볼륨 설정을 위해 사용
- -w
    - 컨테이너의 작업 디렉토리 설정
    - Dockerfile의 WORKDIR 설정 덮어씀
- --entrypoint
    - Dockerfile의 ENTRYPOINT 설정 덮어씀
- --rm
    - 컨테이너가 종료될 때 컨테이너와 관련된 리소스까지 깨끗이 제거
    - 컨테이너를 일회성으로 실행할 때 주로 쓰임


## docker exec 
- 특정 컨테이너 환경에서 명령어를 실행하거나, 컨테이너 내부 환경에 대해 

#### docker exec의 옵션
- --workdir
    - 프로세스가 실행되는 위치 변경
- -e, --end-file
    - 추가 환경변수 지정
- --priviledged
    - 프로세스에 추가적인 권한 부여
- --user
    - 프로세스를 실행하는 사용자 지정

#### docker run vs docker exec
- docker run은 컨테이너를 생성하고 실행
- docker exec은 이미 실행된 특정 컨테이너의 환경을 디버깅




## docker cp
- host 컴퓨터에 있는 파일을 컨테이너에 복사할 수 있는 명령어
- 명령어 예시
    ```shell
    docker cp ~/Downloads/CREATE_USER.sql machbase:/home/machbase
    ```
