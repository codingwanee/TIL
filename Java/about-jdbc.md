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





# JPA (Java Persistence API)
- 자바에서 데이터를 영구히 기록할 수 있는 (DBMS에)환경을 제공하는 API

## ORM (Object Relational Mapping)
- 객체를 데이터베이스의 데이터와 연결해주는 매핑 기술
- java에서와 db에서의 데이터 타입이 다르기 때문에 서로 호환될 수 있도록 object를 만들어야 하는데, 이걸 모델링이라고 한다.

#### ORM의 역할
1. java -> db 연결요청
2. java <- db 인증 후 세션오픈
3. java - connection 유지
4. java -> db 쿼리전송
5. java <- db 데이터를 json으로 생성 후 전송
6. data를 java 객체로 변경

## JPA의 구조
- JPA는 라이브러리가 아닌 인터페이스일 뿐
- 따라서, JPA를 구현하는 라이브러리가 필요
    - Hibernate, OpenJPA 등

#### Hibernate
- 하이버네이트는 JPA 구현체의 한 종류
- Hibernate ORM은 자바 언어를 위한 객체관계매핑 프레임워크

#### Spring Data JPA
- 스프링부트에서는 Spring Data JPA라는 프레임워크를 사용
- 일반적으로 말하는 JPA와 달리, JPA를 더 쉽게 사용하기 위해 스프링에서 제공하고 있는 프레임워크
