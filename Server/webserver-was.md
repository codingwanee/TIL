# Web Server와 WAS
- 참고글: https://gmlwjd9405.github.io/2018/10/27/webserver-vs-was.html

## Web Server
#### Web Server의 개념
1. 하드웨어: Web 서버가 설치되어 있는 컴퓨터
2. 소프트웨어: 웹 브라우저 클라이언트로부터 HTTP 요청을 받아 정적인 컨텐츠(.html .jpeg .css 등)을 제공하는 컴퓨터 프로그램

#### Web Server의 기능
- HTTP 프로토콜을 기반으로 하여 클라이언트(웹 브라우저 또는 웹 크롤러)의 요청을 서비스 하는 기능
- 요청에 따라 아래의 두 가지 기능 중 적절하게 선택하여 수행
    1. 정적 컨텐츠 제공
        - WAS를 거치지 않고 바로 자원 제공
    2. 동적 컨텐츠 요청 전달
        - 클라이언트의 요청(request)을 WAS에 보내고, WAS가 처리한 결과를 클라이언트에 전달(response)
        - 클라이언트란 일반적으로 웹 브라우저를 의미

#### Web Server의 예
- Apache
- Nginx

## WAS (Web Application Server)
#### WAS의 개념
- DB 조회와 같이 다양한 로직 처리를 요구하는 동적 컨텐츠를 제공하기 위해 만들어진 Application Server
- HTTP를 통해 컴퓨터나 장치에 애플리케이션을 수행해주는 미들웨어(소프트웨어 엔진)
- "웹 컨테이너(Web Container)" 혹은 "서블릿 컨테이너(Servlet Container)라고도 불림
    - Container란 JSP, Servlet을 실행시킬 수 있는 소프트웨어
    - 즉, WAS는 JSP, Servlet 구동 환경을 제공

#### WAS의 역할
- WAS = Web Server + Web Container
- Web Server 기능들을 구조적으로 분리하여 처리하고자 하는 목적으로 제시
- 최근에는 WAS가 가지고 있는 Web Server도 정적인 컨텐츠를 처리하는 데 있어서 성능상 큰 차이가 없음

#### WAS의 주요 기능
- 프로그램 실행 환경과 DB 접속 기능 제공
- 여러 개의 트랜잭션(논리적인 작업 단위) 관리 기능
- 업무를 처리하는 비즈니스 로직 수행

#### WAS의 예
- Tomcat
- JBoss
- Jeus
- Web Sphere

## Web Server vs WAS
#### Web Server와 WAS를 분리하는 이유
- 기능 분리 - 서버 부하 방지
    - 정적 콘텐츠와 동적 콘텐츠를 처리하는 기능을 서로 분리하여 서버의 부담을 줄일 수 있음
    - Web Server를 통해 정적인 파일들을 Application Server까지 가지 않고 앞단에서 빠르게 보내줄 수 있다
        - 클라이언트는 요청에 대한 응답으로 HTML 문서를 먼저 받음
        - 그 이후 HTML에 맞게 필요한 정적인 파일(이미지 파일 등)을 다시 서버로 요청 후 받아옴
    - WAS를 통해 비즈니스 로직에 맞도록 그때그때 결과를 만들어 제공함으로써 자원을 효율적으로 사용 
    - 만약 Web Server만을 이용한다면 사용자가 원하는 요청에 대한 결과값을 모두 미리 만들어놓고 서비스를 해야 함
    - 이 경우 자원이 절대적으로 부족하기 때문에, 그때그때 사용자 요청에 맞는 동적 컨텐츠를 만들어 제공하는 것이 효율적
- 물리적 분리 - 보안 강화
    - SSL에 대한 암복호화 처리에 Web Server 사용
- 확장성 - 여러 대의 WAS 연결 가능
    - Load Balancin을 위해서 Web Server를 사용
    - fail over(장애 극복), fail back 처리에 유리
    - 특히 대용량 웹 어플리케이션의 경우(여러 개의 서버 사용), Web Server와 WAS를 분리하여 무중단 운영을 위한 장애 극복에 쉽게 대응 가능
    - 예를 들어 앞 단의 Web Server에서 오류가 발생한 WAS는 이용하지 못하도록 한 뒤 WAS를 재기동 해도 사용자는 오류를 느끼지 못하고 이용 가능
- 여러 웹 어플리케이션 서비스 가능
    - 예를 들어, 하나의 서버에서 PHP Application과 Java Application을 함께 사용하는 경우

#### 일반적인 Web Service Architecture
1. Client -> Web Server -> DB
2. Client -> WAS -> DB
3. Client -> Web Server -> WAS -> DB

- 일반적으로 3번 구조를 많이 볼 수 있는데, 이 구조의 동작과정은 아래와 같다.

1. Web Server는 웹 브라우저 클라이언트로부터 HTTP 요청을 받는다.
2. Web Server는 클라이언트의 요청(Request)을 WAS에 보낸다.
3. WAS는 관련된 Servlet을 메모리에 올린다.
4. WAS는 web.xml을 참조하여 해당 Servlet에 대한 Thread를 생성한다. (Thread Pool 이용)
5. HttpServletRequest와 HttpServletResponse 객체를 생성하여 Servlet에 전달한다.
    - 5-1. Thread는 Servlet의 service() 메서드를 호출한다.
    - 5-2. service() 메서드는 요청에 맞게 doGet() 또는 doPost() 메서드를 호출한다.
    - protected doGet(HttpServletRequest request, HttpServletResponse response)
6. doGet() 또는 doPost() 메서드는 인자에 맞게 생성된 적절한 동적 페이지를 Response 객체에 담아 WAS에 전달한다.
7. WAS는 Response 객체를 HttpResponse 형태로 바꾸어 Web Server에 전달한다.
8. 생성된 Thread를 종료하고, HttpServletRequest와 HttpServletResponse 객체를 제거한다.


## 참고) DBMS와 Middle Ware의 개념
#### DBMS(Database Management System)
- 다수의 사용자들이 DB 내의 데이터를 접근할 수 있도록 해주는 소프트웨어
- DBMS는 보통 Server 형태로 서비스를 제공
- ex) MySQL, MariaDB, Oracle, PostgreSQL 등
- DBMS Server에 직접 접속해서 동작하는 Client Program의 문제점
    - Client에 로직이 많아질수록 Client Program의 크기가 커짐
    - 로직이 변경될 때마다 매번 배포가 되어야 하는 불편함
    - Client에 대부분의 로직이 포함되어 배포가 됨 --> 보안에 취약
    - 위 문제점들을 해결하기 위해 MiddleWare가 등장

#### MiddleWare
- Client - MiddleWare Server - DB Server(DBMS)
- 동작과정
    1. Client는 단순히 요청만 중앙에 있는 MiddleWare Server에게 보낸다.
    2. MiddleWare Server에서 대부분의 로직이 수행된다.
    3. 이때, 데이터를 조작할 일이 있으면 DBMS에 부탁한다.
    4. 로직의 결과를 Client에게 전송한다.
    5. Client는 그 결과를 화면에 보여준다.
    - 즉, 비즈니스 로직을 Client와 DBMS 사이의 MiddleWare Server에서 동작하도록 함으로써 Client는 입력과 출력만 담당하게 된다.

