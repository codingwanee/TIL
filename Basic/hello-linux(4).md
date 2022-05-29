# 리눅스 - 사용자

## 다중사용자
* 리눅스는 사용자 별로 기능, 권한을 다르게 부여할 수 있다.

#### id
* 사용자 또는 그룹을 구분하는 고유 식별자
* uid : user id
* gid : group id
* who : 현재 접속중인 사용자를 알 수 있는 명령어

## 관리자와 일반 사용자
* 사용자의 종류
    * super(root) user : 관리자
    * user : 일반 유저
* 최상위 디렉토리의 차이
    * cd /root : 루트 사용자(관리자)의 최상위 디렉토리
    * cd ~ : 일반 사용자의 최상위 디렉토리(home)
* sudo 뒤에 명령어를 사용하면 관리자 권한으로 실행 가능
* 터미널에서 관리자로 접속하면 보통 이름이 root라고 표시된다
* 터미널의 입력창에 이름 뒤 기호의 의미
    * # : 관리자로 접속중
    * $ : 일반 유저로 접속중
* su - 또는 su - root 를 적으면 관리자로 접속 변경
* 관리자로 접속을 막아놓는 os도 있다(ubuntu 등) 이 경우 권한변경을 통해 접속 가능하게 변경 필요
    * sudo passwd -u root : 관리자 계정에 비밀번호를 설정하는 명령어
    * sudo passwd -l root : 관리자 권한에 락을 거는 명령어



## 사용자의 추가

#### useradd
* sudo useradd -m wanee
    * wanee 라는 사용자를 추가하는 명령어
    * 사용자를 추가하는 것은 sudo로 실행해야 함
* sudo usermod -a -G sudo wanee
    * wanee 사용자에게 sudo 명령어를 쓸 수 있도록 권한을 부여하는 명령어



## 권한 (permission)

#### 사용자 권한 읽는 방법
```shell
# test.txt의 권한 예시
-rw-rw-r-- 1 wanee wanee 0 Dec    4 23:19 test.txt 
```
* 각 자리의 의미
    * -/---/---/--- : 타입/파일소유자(u)/그룹(g)/그 외(o)  의 권한을 나타냄
    * type : 파일 또는 디렉토리
    * r : read
    * w : write
    * x : execute

#### 권한 변경
* chmod : change mode, 즉 모드변경 명령어로 파일의 사용자/그룹에 대한 권한 변경 가능
* chmod o-r test.txt
    * test.txt라는 파일의 other들의 권한에서 r을 제외시키겠다
    * o-r 대신 o+r이라고 쓰면 권한을 부여하겠다는 뜻
* 실행 권한부여 실습을 위한 실행가능한 파일 예시
    ```shell
    #!/bin/bash
    echo hi hi hi
    ```
    * 위 소스를 작성하고 /bin/bash hi-example.sh 라고 치면 실행됨
    * 그러나 그냥 현재 위치에서 ./hi-example.sh 로 실행시 오류 발생
    * 파일소유자(현재 접속 유저)에게 실행권한이 없기 때문
    * chmod u+x hi-example.sh; 라고 권한변경 후 실행하면 정상 작동하는 것 확인

#### directory의 권한
* 파일과 같은 방식으로 권한 부여
    * chmod o+r dir1 : dir1 디렉토리에 others의 읽기권한 부여
* chmod -R o+w dir1
    * -R 은 recursive, 재귀적
    * 따라서 dir1 아래에 다른 디렉토리가 있다면 모두 동일하게 권한 적용

#### chmod 사용법 정리
```shell
chmod [options] mode[, mode] 
```
* Octal modes (8진수 모드)
    * 0~7의 숫자에 각기 권한을 매핑해 한 번의 명령어로 권한을 쉽게 변경할 수 있게 함
    * (ex) 0: ---, 1: --x, 7: rwx 등
    * chmod 335 test.txt 처럼 옵션 자리에 숫자로 표현 (사용자, 그룹, 그 외 순)



## 그룹
* 사용자를 묶어 그룹을 만들고 이름을 지정해 권한을 다르게 부여할 수 있다.

#### groupadd
* sudo useradd -G {group-name} username
    * 사용자를 생성한 뒤 특정 그룹에 추가
* sudo usermod -a -G groupname username 
    * 이미 존재하는 사용자를 특정 그룹에 추가
* sudo groupadd groupname
    * 신규 그룹 생성
