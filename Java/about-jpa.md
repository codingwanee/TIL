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

