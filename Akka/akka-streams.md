# Akka Streams

* Akka Steams는 액터 기반 동시성 모델을 사용하는 Akka 툴킷 위에 Reactive Streams 사양을 구현한 것이다.
* Reactive Stream은 비동기식, 비블로킹, 이벤트 기반 데이터 처리에 중점을 두고 만들어졌다.

## 비동기 프로세싱

* 한 문장으로 정리하면 Akka Streams는 액터 기반 동시성 모델을 사용하는 Akka 툴킷 위에 Reactive Steams 사양을 구현한 것
* Reactive Stream 스펙은 비동기식, 비 블로킹, 이벤트 기반 데이터 처리에 관심이있는 여러 회사들이 모여 시스템 경계와 기술 스택 전반에 걸쳐 소통,적용 할 수 있도록 만들어짐



#### 비동기 프로세싱
* 시스템 리소스를 효율적으로 사용하면서 메시지의 생산자와 소비자 간의 처리 속도를 조정하는 기능
* 생산자가 빠른속도로 전송하는 메시지는 처리가 느린 소비자에게는 잠재적으로 문제를 일으킬 수 있음
* 과거에는 소비자가 자신의 페이스대로 메시지를 처리하기를 기다리는 동안 생산자를 차단함으로써 일반적인 back pressure 를 수행
* 시스템 간 메시지의 동기 처리에 의존하는 이 접근법은 비효율적이며 비동기 처리 (훨씬 뛰어난 확장성 과 자원 활용)의 장점을 무효로 하므로 back pressure 을 구현하기 위한 논블럭 솔루션이 필요
* reactive stream 과 관련하여 back pressure 은 비동기 처리 모델의 필수 요소이며 비동기 메시지 전달을 통해 구현



## Source
* 소스는 스트림의 시작이며, 시작 스트림을 통해 데이터가 흐르기 시작한다.
* 소스는 원격 TCP 소켓, 콜렉션, 데이터베이스 조회 또는 HTTP 요청과 같은 메시지를 생성할 수 있는 모든 것이 될 수 있다.

## Sink

## Flow
