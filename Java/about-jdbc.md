# JDBC

## JDBC
- JDBC(Java Database Connectivity)
- 자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API
- JDBC는 데이터베이스에서 자료를 쿼리하거나 업데이트 하는 방법을 제공

#### 역사
- 1997년 2월 19일 썬 마이크로시스템즈에서 JDK 1.1의 일부로 JDBC 출시(이후로 자바 SE의 일부에 포함)
- JDBC 클래스는 자바 패키지 java.sql과 javax.sql에 포함
- 버전 3.1을 기점으로 JDBC
- 최신 버전은 JDBC 4.2

## JDBC driver
- DB 벤더사에서는 자신의 DB에 맞도록 JDBC 인터페이스를 구현해서 제공하는 라이브러리
- DBMS와 통신을 담당하는 자바 클래스
- DBMS별로 알맞은 JDBC 드라이버 필요(jar)

#### JDBC 작동 순서
1. JDBC 드라이버 로드
2. DB 연결 오픈
3. 쿼리문으로 DB data i/o
4. DB 연결 종료

#### JDBC URL
- DBMS와의 연결을 위한 식별값
- JDBC 드라이버에 따라 형식이 다름
