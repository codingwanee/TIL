# WebFlux


## 배경
- 인터넷의 발달로 점점 트래픽이 증가하며, 기존과 다른 패러다임의 동시성 처리가 필요해짐
- 기존 Spring MVC 에서는 '스레드풀'로 동시성을 처리
- 넷플릭스 예시
  - threadpool hell --> API 아키텍처 재설계로 등장한 게 'rxJava' (따로 설명)
- WebFlux는 기존 방식을 개선에 적은 스레드로 동시성을 처리


## WebFlux의 방식
- 쓰레드의 blocking 없이 
- non-blocking은 자신이 호출됐을 때 제어권을 바로 자신을 호출한 쪽으로 다시 넘김


## Reactive Programming
- 반응형 프로그래밍
- non-blockiongI/O에 Reactive Stream, backpressure를 곁들인 것



#### Reactive Stream
- Asyncronous, Non-blockingdm로 작동하는 Stream
- Publisher에서 변경이 생기면 Subscriber에 변경된 데이터들을 Stream으로 전달
- 이 Stream으로 프로그래밍하는 패러다임이 Reactive Programming (모든 것은 Stream이다!)



#### Backpressure
- subscriber로 들어오는 Stream 양을 조정, 적은 컴퓨팅 자원으로 일을 처리하기 가능한 정도씩만 받기
- 
