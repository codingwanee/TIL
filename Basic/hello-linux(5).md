# 리눅스

## 인터넷, 네트워크 그리고 서버

#### internet, network, server
* client <------> server
* client의 request를 server가 받아 처리한 뒤 response를 보내는 게 인터넷의 원리
* google.com 과 같은 주소를 '도메인'이라고 함
* 터미널에서 ping google.com 이라고 하면 숫자로 된 ip adress를 보여준다.
* 인터넷 접속 원리
    * client가 domain으로 접속 시도
    * DNS server 가 도메인을 ip adress로 변환
    * 해당하는 ip adress로 접속

#### ip, domain
* ip addr
    * 본인 컴퓨터의 ip 주소를 알 수 있는 명령어
    * 컴퓨터의 실제 ip를 알려줌
* curl : 웹사이트에 접속해서 정보를 받아오는 명령어
    * 터미널에서 curl ipinfo.io/ip 명령어로 본인의 ip주소를 알 수도 있음
    * ipinfo.io/ip : 접속 시 본인 컴퓨터의 ip 주소를 알려주는 사이트
* (참고) 맥 터미널에서는 ipconfig getifaddr en0 명령어로 확인 가능
* ip addr 라는 명령어와 curl ipinfo.io/ip 명령어로 알게 된 ip주소가 서로 다를 수도 있음
* 실제 자신의 ip와 외부에 접속할 때 이용하는 ip가 다를 수도 있기 때문(ipinfo.io/ip는 최종적인 ip를 알려줌)
* 통신사 회선을 통해 외부 인터넷과 접속을 하기 때문에 다른 것
* 일반적으로 한 가정에서 여러 대의 기기를 이용하는 경우
    * 기기당 회선 계약을 맺고 ip 주소를 부여받으면 비쌈
    * 따라서 보통 1개의 회선을 계약하고(public address) 공유기(router)로 내부적으로 분배(private address)
* public ip와 private ip가 같은 경우, 자신의 컴퓨터를 그대로 서버로 사용 가능
* 그러나 pub, pri의 ip가 서로 다른 경우 그대로 서버로 이용할 수 없음
* 공유기를 써서 ip의 앞자리가 같은 경우 서로 서버통신 가능


## 웹서버 (아파치)

#### 클라이언트 - 서버 통신과정
1. client가 server에게 index.html 요청
2. server는 서버와 연결된 저장장치 어딘가에서 index.html을 찾아 응답
3. client는 응답을 화면에 출력


#### Apache를 통한 서버 만들기 (강의 - ubuntu)
* apache는 간편하게 사용할 수 있는 웹서버

1. 아파치 설치
    * sudo apt-get apache2
    * 설치 후 htop 접속 > f4 (filter) > apache로 프로세스 확인
2. elinks 설치
    * sudo apt-get elinks
    * 쉘 환경에서 웹 브라우징을 할 수 있는 도구
3. 아파치 실행
    * sudo apache service apache2 start/stop/restart
4. elinks로 아파치 동작 확인
    * elinks http://자신의ip주소/
    * ubuntu apache2 ~~ It works! 어쩌구 화면이 나오면 성공
5. /etc/apache2 밑의 설정파일을 확인해서 기본 접속 페이지 확인
    * /etc는 각 프로그램에 대한 설정파일들이 저장된 곳
    * /etc/apache2/apache2.conf 확인
        * /etc/apache2/sites-enabled/ 디렉토리 아래 config 파일을 참고하겠다는 내용 확인
        * 해당 디렉토리의 000-default.conf 파일 확인
        * /var/www/html/ 디렉토리 확인
        * 해당 디렉토리의 index.html 내용대로 출력되는지 확인
 


#### 맥 환경에 맞게 실습 따라하기
1. 아파치 내장 확인
    * apachectl -v
2. elinks 설치 (homebrew 이용)
    * brew install elinks
3. 아파치 실행
    * sudo apachectl start
4. elinks로 접속 확인    
    * elinks http://자신의ip주소/  -- ip주소 대신 localhost 라고 적어도 됨
    * It works! 라는 문구가 뜨면 정상작동



## 원격제어 (ssh)
* 서버 컴퓨터에 SSH server 설치
* 클라이언트 컴퓨터에 SSH client 설치

#### SSH 동작원리
1. 클라이언트 컴퓨터가 SSH client를 통해 SSH server에 명령을 전달
2. SSH server는 자신이 설치된 컴퓨터에 요청을 전달
3. SSH server는 컴퓨터의 처리 결과를 받아 SSH client에 응답

#### 실습 따라하기 (ubuntu)
1. 기존 설치된 ssh 서버, 클라이언트 삭제
    * sudo apt-get purge openssh-server openssh-client
2. ssh 서버, 클라이언트 설치
    * sudo apt-get install openssh-server openssh-client
3. ssh 서버 시작
    * sudo service ssh start
    * sudo ps aux | grep ssh
4. 클라이언트 컴퓨터에서 ssh 서버 접속
    * ssh egoing@192.168.0.65 (사용자id@본인의 ipwnth)
5. -p를 넣어 포트를 입력해서 접속해봄 (ssh의 포트는 22)
    * ssh -p 22 egoing@192.168.0.65 : 정상 접속
    * ssh -p 2222 egoing@192.168.0.65 : 접속 안됨
    * 포트번호는 반드시 22일 필요 없이 /etc/ssh/sshd_config에서 변경 가능
6. exit를 눌러 서버 접속 종료


## 포트 (port)
* 포트는 서버에 접속할 때 사용하는 
* ~1024 까지는 목적에 따라 표준처럼 통용되는 well-known 포트가 있음.
    * 웹서버 : 80
    * ssh  : 22
    * 1024~65만 : 사용자가 마음대로 지정 가능
    * 예를들어 웹사이트 접속 시 포트는 :80
    * 이런 기본 포트번호는 생략 가능하지만, 다른 번호를 입력시 에러 발생
* 서버는 해당 포트로 들어오는 요청을 계속 Listen 하고 있다.


#### Port forwarding
1. ISP(Internet Service Provider) 즉 SKT, KT와 같은 인터넷 회선 제공회사들과 회선 계약을 맺는다. = public ip
2. 공유기, 즉 Router로 하나의 회선을 여러 개로 나누어 디바이스별로 연결해 사용한다. = private ip
3. public ip까지는 외부에서 접속 가능하지만 private ip는 단순히 ip 주소를 알아도 외부에서 접속할 수 없다.
4. 외부에서 private ip로 접속하기 위해 port forwarding가 필요하다. 즉 정해진 port로 접속하면 매핑된 내부 ip로 접속하도록 포워딩 해주는 것

#### default gateway
* 공유기에는 기본적으로 web server가 깔려 있다.
* 공유기 안쪽에서만 통용되는 Ip 주소를 default gateway라고 한다.
* ip route 명령어로 자신의 default gateway를 알 수 있다.

#### 맥에서 route 설정을 통한 port forwarding
1. 시스템 환경설정> 네트워크> advanced> 라우터의 주소 확인 (나는 192.168.35.1)
2. 사파리 주소창에 http://192.168.35.1 입력하면 라우터 관리 화면이 뜸
3. 로그인이 가능하다면 관리(설정)에서 '포터포워드 설정'
4. 정책 추가
    * 외부포트: 외부 클라이언트가 접속할 때 필요한 포트
    * 내부IP: private ip
    * 내부포트: 내부 ip가 요청을 보낼 port



## 도메인 (domain)

#### DNS : Domain Name Server


#### hosts
* 어떤 도메인이 어떤 IP에 해당하는지 정보를 담은 파일
* DNS가 등장하기 전 도메인을 이용했던 오래전 방법
* (맥) /etc/hosts 에 IP와 도메인의 매핑을 작성 가능 - DNS보다 우선순위

#### 도메인 구입
* 오늘날 도메인은 ICANN이라는 민간 업체에 의해 관리되고 있다.
* 특정 도메인을 이용하기 위해서는 원하는 도메인이 이미 이용중인지 확인 후 구매한다.
* 서브도메인을 통해 여러 개의 사이트 주소를 보유한 것 같은 효과를 낼 수도 있다.

#### DNS principle
* google.com 뒤에는 . 이 생략되어 있다. 이것이 root DNS
* root DNS server는 전 세계에 분산되어 존재하며, 클라이언트 쪽에 root DNS server로 접속하는 주소가 내장되어 있음.

* google.com에 접속하는 과정
    1. 클라이언트가 도메인 주소를 입력
    2. root DNS server가 도메인 서버의 목록을 응답(분산되어 있으므로 여러 대)
        * 도메인: .com
    3. 클라이언트가 해당 도메인을 담당하는 DNS에 다시 도메인 주소를 보냄
    4. 도메인 이름(google)을 담당하는 도메인 네임 서버의 목록을 응답
        * 도메인 이름: google
        * DNS 서버를 자신의 서버에 직접 설치하는 방법도 있지만 최근에는 줄어드는 추세
    5. 서버의 목록을 통해 실제 IP 주소를 응답받음
    6. 해당 IP로 접속   
* 터미널에 dig +trace google.com 입력
    * google.com의 IP 172.217.175.238에 접속하기 위해 거쳐가는 도메인 서버를 확인 가능


## 인터넷을 통한 서버간 동기화 (rsync)
* remote sync, 즉 원격으로 서버 간 동기화하는 기술
* 변경사항만 동기화 시킬 수 있어 간편하고 효율적인 방식

#### 실습 (1) - 같은 컴퓨터 내에서
1. 홈 디렉토리에 rsync 디렉토리 생성 후 하위에 dir1, dest 디렉토리 생성
    * mkdir rsync; cd rsync; mkdir dir1; mkdir dest
2. src 디렉토리 하위에 test test1~10 파일 생성
    * touch test{1..10}
3. rsync 디렉토리로 돌아와 src 하위의 내용을 dest에 동기화
    * rsync -a src dest : src 디렉토리 생성과 함께 동기화
    * rsync -a src/ dest : src 디렉토리 하위의 내용 동기화

#### 실습 (2) - 서로 다른 컴퓨터 내에서
* rsync -azP ~/rsync/src wanee@192.168.0.65:~/rsync/dest


#### rsync 옵션
* -av : 동기화 할 변경사항을 함께 출력
* -az : 데이터를 압축해서 전송(zip)
* -azP : 전송 내용을 바로 보여줌(Progress)

