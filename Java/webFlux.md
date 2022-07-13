# WebFlux


## 배경
- 인터넷의 발달로 점점 트래픽이 증가하며, 기존과 다른 패러다임의 동시성 처리가 필요해짐
- 기존 Spring MVC 에서는 '스레드풀'로 동시성을 처리
- 넷플릭스 예시
  - threadpool hell --> API 아키텍처 재설계로 등장한 게 'rxJava' (따로 설명)
- WebFlux는 기존 방식을 개선에 적은 스레드로 동시성을 처리


#### 기존 Spring 블로킹 방식
- 서버가 응답을 보낼 때, 작업이 오래 걸릴 경우에는 요청에 대한 응답이 모두 종료될 때까지 블로킹
- 따라서 Spring에서는 동시 요청 처리를 위해 멀티 thread를 지원
- 그러면 하나의 thread가 하나의 요청을 처리
- 결국 thread가 늘어날 수록 thread 할당에 필요한 리소스가 늘어나 비효율적이 될 수도 있음


#### Spring5의 논블로킹 방식
- Sprin5부터 클라이언트의 요청에 별도의 thread를 생성하지 않고 buffer를 사용해서 요청을 받고, 뒤에서 처리하는 thread는 여러개를 두어서 처리


## WebFlux의 방식
- 쓰레드의 blocking 없이 
- non-blocking은 자신이 호출됐을 때 제어권을 바로 자신을 호출한 쪽으로 다시 넘김

- flux는 stream과 동의어로 '흐름'을 뜻한다
- webflux는 구독과 출판의 개념을 가지고 있다

- 구독 : response가 유지되고 있다
- 출판 : 유지되고 있는 선으로 계속적으로 데이터를 응답해준다


## Reactive Programming
- 반응형 프로그래밍
- 비동기 블로킹 프로세스로 동작하는 애플리케이션을 논블록킹 프로세스로 동작하기 위해 지원하는 프로그래밍
- non-blockiongI/O에 Reactive Stream, backpressure를 곁들인 것


## Mono와 Flux
- Mono와 Flux는 Reactor 라이브러리의 주요 객체
- Mono는 0-1개의 결과만을 처리하기 위한 Reactor 객체
- Flux는 0-N개의 결과물을 처리하기 위한 Reactor 객체
- 보통 여러 스트림을 하나의 결과를 모아줄 때 Mono를 쓰고 각각의 Mono를 합쳐서 하나의 여러 개의 값을 여러개의 값을 처리할 떄 Flux를 사용


#### Reactive Stream
- Asyncronous, Non-blockingdm로 작동하는 Stream
- 비동기 스트림 처리를 위한 표준
- Publisher가 전송하면 데이터는 sequence 대로 전송한다. 그러면 Subscriber가 데이터를 수신하고 next, complete, error 발생
- Publisher에서 변경이 생기면 Subscriber에 변경된 데이터들을 Stream으로 전달
- 이 Stream으로 프로그래밍하는 패러다임이 Reactive Programming (모든 것은 Stream이다!)



#### Backpressure
- subscriber로 들어오는 Stream 양을 조정, 적은 컴퓨팅 자원으로 일을 처리하기 가능한 정도씩만 받기



## 리액터의 스케쥴러
- Scheduler Worker를 이용해 스레딩에 대한 사용자 제어를 제공하는 인터페이스
- reactor.core.scheduler에 존재
- Schedulers.elastic() 과 Schedulers.boundedElastic() 은 블로킹 IO 태스크와 같은 생명주기가 긴 태스크들에 적합
- elastic 은 요청 할때마다 제한 없이 스레드를 생성한다. 최근에 도입된 boundedElastic 은 같은 기능을 하지만 스레드 수가 제한됨


#### Schedulers가 제공하는 팩토리 메서드들 (일종의 스레드풀)
- parallel():  ExecutorService기반으로 단일 스레드 고정 크기(Fixed) 스레드 풀을 사용하여 병렬 작업에 적합
- single(): Runnable을 사용하여 지연이 적은 일회성 작업에 최적화
- elastic(): 스레드 갯수는 무한정으로 증가. 수행시간이 오래걸리는 블로킹 작업에 대한 대안으로 사용할 수 있게 최적화
- boundedElastic(): 스레드 갯수가 정해져있고 elastic과 동일하게 수행시간이 오래걸리는 블로킹 작업에 대한 대안으로 사용할 수 있게 최적화
- immediate(): 호출자의 스레드를 즉시 실행
- fromExecutorService(ExecutorService) : 새로운 Excutors 인스턴스를 생성
