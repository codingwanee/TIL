# 리눅스 

## 백그라운드 실행
* ctrl + z : 실행중인 프로그램을 백그라운드로 보내는 단축키
* ~ $ : 명령어 뒤에 $를 붙이면 백그라운드로 실행됨
* jobs : 백그라운드 작업들의 목록을 보여줌
* fg %n : jobs로 출력된 백그라운드 프로그램 목록 중에서 원하는 번호의 프로세스를 fg로 올림
* kill %n : 원하는 번호의 프로세스 종료


## 항상 실행 (deamon, service)

#### deamon
* 항상 실행되고 있는 프로세스
* 모뎀, 현관문 잠금장치처럼 상시로 켜져있는 상태. 특히 서버가 그렇다

#### service
* 데몬 프로그램들의 저장은 보통 /etc/init.d 에 위치
* sudo service 프로그램 start/stop
    * 데몬 프로그램을 시작/종료하는 명령어
* /etc

* /etc/rc3.d를 보면 S로 시작하는 여러 파일들이 보이는데, 실제 파일이 아닌 링크
    * S 로 시작 - 부팅될 때 자동으로 실행되는 deamon
    * K 로 시작 - 부팅할 때 자동실행 X


## 정기적으로 실행 (cron)

#### cron
* 어떤 작업을 정기적으로 실행시켜주는 작업 예약 스케줄러
* crontab -e
    * 수행할 작업을 작성할 수 있는 창이 에디터로 열린다.
* crontab expression
    * 표현식으로 반복 주기를 나타낼 수 있다.
    ```shell
    # m h dom mon dow command
    * * * * * date >> date.log
    ```
    * minute (0~59)
    * hour (0~23)
    * dom (= day of month) (1~31)
    * month (1~12)
    * dow (= day of week) (0~6) (sumday = 0)


#### 실습예제
1. crontab -e
2. 아래와 같이 입력 후 ctrl + g
    ex) */1 * * * * date >> date.log 2>1
3. tail -f date.log 로 1분마다 내용이 갱신되는지 확인

* date : 현재 시간을 출력
* tail -f 파일명
    * -f 옵션을 붙이면 파일의 끝 부분을 출력할 뿔만 아니라, 파일을 모니터링 해서 업데이트 되는 부분도 출력
* 출력 옵션
    * 1> : 표준출력 출력
    * 2> : 표준에러 출력
    * 2>1 : 표준에러를 표준출력으로 바꿔서 출력

#### (추가) 표현식 예제
* 참고한 글  https://jdm.kr/blog/2
* 참고한 글2 https://jdm.kr/blog/4

```shell
# 매분 test.sh 실행
* * * * * /home/script/test.sh

# 매주 금요일 오전 5시 45분에 test.sh 를 실행
45 5 * * 5 /home/script/test.sh

# 매일 1시 0분부터 30분까지 매분 tesh.sh 를 실행
0-30 1 * * * /home/script/test.sh

# 매 10분마다 test.sh 를 실행
*/10 * * * * /home/script/test.sh

```


## 쉘을 시작할 때 실행

#### startup script
* alias
    * 특정 명령어에 별명을 부여해서 단축키처럼 쓸 수 있음
    * alias l='ls -al'
* 홈 디렉토리에서 자신의 쉘 프로그램에 따라 .bashrc 또는 nano .zshrc 등을 입력
* 해당 쉘 프로그램을 실행할 때마다 수행하고 싶은 명령어를 입력 후 저장
