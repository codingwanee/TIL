# Logback
* logback은 로그를 관리하기 위한 프레임워크
* 기존에 널리 쓰던 log4j보다 더 안정성 높고 편리한 Slf4j의 구현체
* Logback은 spring boot의 기본 로깅 프레임워크기도 하다.


## Logback 특징
* 빠른 implimentation
* 적은 메모리 점유
* XML로 설정
* 사용자별로 로깅 level 지정 가능


## Logback 구조
* Logger : 로그의 주체, 로그의 메시지 전달, 특정 패키지 않의 특정 레벨 이상인 것에 대해 출력
* Appender : where, 어디에 출력할지에 대해 기술
* Encoder : how, 어떻게 출력할지에 대해 기술


## Logback 설정 (java-spring 예시)

* java-spring에서
  * pom.xml에 라이브러리 추가
  * src/main/resources 하단에 logback.xml 파일을 생성한 후 다음과 같이 Logback을 설정


* scala-sbt 에서
  * src/main/resources/logback.xml 파일에 아래와 같이 작성
  * 참고 출처 https://github.com/x3ro/scala-sbt-logback-example/blob/master/src/main/resources/logback.xml
  ```xml
  <configuration>
      <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
          <encoder>
              <pattern>%d{HH:mm:ss.SSS} TKD [%thread] %-5level %logger{36} - %msg%n</pattern>
          </encoder>
      </appender>

      <appender name="FILE" class="ch.qos.logback.core.FileAppender">
          <file>/tmp/test.log</file>
          <append>true</append>
          <encoder>
              <pattern>%d{HH:mm:ss.SSS} TKD [%thread] %-5level %logger{36} - %msg%n</pattern>
          </encoder>
      </appender>

      <root level="debug">
          <!--<appender-ref ref="STDOUT" />-->
          <appender-ref ref="FILE" />
      </root>
  </configuration>
  ```
