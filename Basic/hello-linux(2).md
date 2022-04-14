# Linux

## IO Redirection


#### output
* 리눅스에서 IO Redirection이라는 것은, 원래 모니터에 출력되어야 할 output을 다른 프로세스의 input으로 넣어주는 것
* 모니터로 출력될 값을 다른 프로세스로 redirection 한다는 것이다.
* IO값 지정  >
    * ls -l > test.txt
        * ls -l의 결과를 test.txt 파일에 넣어준다
* UNIX는 unix process의 input과 output에 대한 정의가 되어있다.
* 보통 한 명령어를 실행한 뒤 화면에 출력되는 결과를 standard output, 에러가 나면 standard error이다.
* 옵션 >는 standard error를 인풋으로 받지 않기 때문에 앞 명령어가 error인 경우 output을 따로 처리하지 않고 에러가 난다.(즉 output이 redirection 되지 않음)
* 2> 옵션을 사용하면 에러 결과를 output으로 이용할 수 있다 
* cat
    * 사용자가 키보드로 입력한 정보를 standard input으로 받아 화면에 그대로 출력
* cat 파일명
    * 파일의 내용을 화면에 출력해주는 명령어
* head -n1 < linux.txt > test.text
    * head : 윗부분만 출력하겠다
    * -n1 : 한 줄만 출력하겠다(모니터상 한 줄이 아닌, 개행 기준)
    * 꺽쇠의 방향으로 input, output을 표현 가능
    * 순차적으로 output > input 을 계속 전달 가능

* << 또는 >>
    * 명령어 뒤에 나오는 파일에 추가(=append)할 때 사용
    * mail abc@gmail.com  << eot
        * abc@gmail.com 이라는 메일주소로 메일 작성이 가능
        * 위 명령어를 입력 뒤 원하는 메일 내용을 작성하고 eot라는 행을 입력하면 메일의 내용이 완성
        * eot 대신에 다른 키워드를 입력해도 상관 없음.


## 쉘과 커널

#### 쉘
* 껍데기
* 사용자의 명령어를 받아 커널에 전달 -> 하드웨어에 전달
* 쉘은 다양한 종류가 있다.

#### 커널
* 알멩이
* 하드웨어를 직접적으로 제어하는 핵심적 역할
* 하드웨어의 실행 결과를 쉘에게 전달 -> 사용자에게 전달 

#### bash vs zsh
* echo $0 이라고 입력하면 현재 자신이 사용중인 쉘의 종류를 확인 가능
* bash와 zsh는 모두 널리 쓰이는 쉘의 종류 중 하나이다.
* bash
    * cd 를 입력하고 tab을 누르면 숨김파일까지 후보로 보여줌
* zsh
    * 일반적으로 bash보다 더 편리하다는 평을 받고 있다.
    * 디렉토리를 입력할 때 tab의 자동완성 기능이 더 강력
        * /h/u/ -- tab --> /home/ubuntu 이런식으로 자동완성
    * 현재 디렉토리에서 일부만 바꿔 이동하고 싶을 때 cd dir1 dir2 라고 적으면 바로 이동 가능    

## 쉘 스크립트
* 순차적으로 실행해야 하는 명령어들을 미리 작성해두고 실행시킬 수 있도록 하는 스크립트
* 아스테릭(*)을 이용해서 여러 파일을 선택 가능
ex) ls *.log    # .log로 끝나는 모든 파일 출력 가능

#### 쉘 스크립트 사례
* ~/script/bin
    * 리눅스 기본 명령어들이 저장된 곳
    * 리눅스에서 쓰는 여러 명령어들도 파일의 형태로 이루어져 있음

* #!/bin/bash
    * 스크립트 가장 윗줄에 작성
    * #! 뒤의 디렉토리 아래 있는 프로그램을 통해 해석된다는 약속
    
* 스크립트 작성 예시
* vi 파일명 으로 작성시작 가능
```shell
#!/bin/bash

if [ -d bak ]; then     # 현재 디렉토리에 bak라는 폴더가 존재한다면
    mkdir bak           # bak 폴더를 만든다
fi                      # 조건문 종료
cp *.log bak            
```
    
* chmod +x 파일명
    * x는 execut, 즉 실행 가능하다는 파일 속성


## 디렉토리의 구조
* 리눅스 및 유닉스 계열 os의 디렉토리 구조   

* / = root
    * 모든 디렉토리의 최상위

* /bin = User Binaries
    * bin은 binary의 준말
    * 사용자가 이용하는 기본 명령어가 파일 형태로 저장되어 있음
* /lib
    * /bin등 파일들이 공통으로 사용하는 라이브러리 저장

* /usr
    * /usr/bin
        * 번들 상태로 설치되는 파일들은 여기에 파일
        * 쉽게 말해 옛날에 부팅에 필요하지 않은 파일들은 여기에 저장되었음
        * 최근에는 사용자를 위한 디렉토리는 /home으로 통합되어 가는 추세
    * /usr/sbin
    * /usr/lib


* /home  = Home Directories
    * 사용자
    * cd ~ 했을 때 이동하는 디렉토리


* /etc = Configuration Files
    * 컴퓨터가 동작하는 설정
    * vi로 열어 설정 변경 가능

* /var  = Variable Files
    * syslog, apt, auth.log 등 내용이 계속 바뀌는 것들이 주로 들어있음

* /tmp   = Temporary Files
    * 컴퓨터를 재부팅하면 사라지는 임시 파일들이 주로 저장


* /opt   = Optional add-on Applications
    * 어디에 설치할지 애매한 것들 이곳에 주로 저장


## 프로세스

#### memory
* 가격이 비쌈 = 저장용량이 작음
* 처리속도가 빠름
* RAM

#### storage
* 가격이 쌈 = 저장용량이 큼
* 처리속도 느림
* SSD, HDD

#### processor
* 프로그램들은 파일 형태로 storage에 저장되어 있음
* 프로그램을 실행하면 memory에 적재됨
* 실행중인 프로그램을 process 라고 하고, 이걸 처리하는 것이 processor

#### 프로세스 모니터링

* ps
    * process를 출력해 확인 가능
    * ps aux | grep keyword
        * 원하는 keyword로 프로세스 검색
* sudo top
    * process list를 확인할 수 있는 프로그램
    * htop 이라는 프로그램을 따로 깔면 더 시각적으로 확인 가능
        * RES : 물리적인 메모리 크기
        * MEM% : 메모리 사용률
        * command : 어떤 명령어로 실행되었는지
        * load average : 처리해야 할 프로세스 개수, 예상 처리 소요시간 등 알려줌
* kill
    * 실행중인 프로세스를 강제로 중지시키는 명령어


## 파일을 찾는 법

#### locate
* 성능적으로 빠르게 파일 검색 가능
* 디렉토리로 검색하지 않고 mlocate라는 데이터베이스를 검색
* sudo updatedb
    * db를 업데이트 해주는 명령어
    * 보통 하루에 한 번씩 정기적으로 처리하게 되어있어 따로 해줄 필요는 없음


#### find
* 디렉토리로 검색
* 속도는 db검색보다 느리지만 현재 실제 디렉토리 검색 가능
