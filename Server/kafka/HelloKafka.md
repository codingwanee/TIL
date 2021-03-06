# Kafka


# Kafka



## 배경지식: 메시징 시스템

* Publisher와 Subscriber로 이루어진 비동기 메시징 전송 방식



#### 용어정리

* Message: 데이터 단위
* Broker : 카프카 애플리케이션 서버 단위
* Topic : 데이터 분리 단위. 다수 파티션 보유
* Partition : 레코드를 담고 있음. 컨슈머 요청시 레코드 전달
* Offset : 각 레코드당 파티션에 할당된 고유 번호
* Consumer : 레코드를 polling, 즉 수신하는 애플리케이션 = Subscriber
  *  Consumer group : 다수 컨슈머 묶음
  *  Consumer offset : 특정 컨슈머가 가져간 레코드의 번호
* Producer : 레코드를 브로커로 전송하는 애플리케이션 = Publisher
* Replication : 파티션 복제 기능
* ISR : 리더+팔로워 파티션의 sync가 된 묶음
* Rack-awareness : Server rack 이슈에 대응


#### 작동방식

* 발신자의 메시지에는 수신자가 정해져 있지 않은 상태로 발행(pulbish)
* 구독(Subscribe)을 신청한 수신자만이 정해진 메시지를 받음
* 수신자는 발신자 정보가 없어도 메시지 수신이 가능
* 프로듀서가 메시지를 컨슈머에게 직접 전달하는 것이 아니라 중간의 메시징 시스템에 전달(수신처ID 포함)
* 메시징 시스템의 교환기가 메시지의 수신처ID 값을 통해 컨슈머들의 큐에 메시지 전달(push)
* 컨슈머는 큐를 모니터링 하다가 큐에 메시지가 있을 경우 값을 회수

#### 특징

* 특정 개체가 수신불능 상태가 되더라도 메시지가 유실되지 않음
* 메시징 시스템으로 연결되어 있기 때문에 확장성이 용이
* 직접 통신하지 않기 때문에 메시지가 잘 전달되었는지 파악 힘듬
* 중간에 메시징 시스템을 거치기 때문에 메시지 전달 속도가 빠르지 않음



## 배경지식: MQ

* MQ란 메시지 큐(MessageQueue)를 나타내는 말로 프로세스 또는 프로그램 인스턴스가 데이터를 서로 교환할때 사용하는 통신 방법이다.
* 더 큰 의미로는 메시지 지향 미들웨어(Message Oriented Middleware: MOM)를 구현한 시스템이다. MOM이란 비동기 메시지를 사용하는 응용 프로그램 간의 데이터 송수신을 말한다.



#### 메시지 큐 사용처

* 메시지 큐는 다음과 같이 다양한 곳에 사용이 가능하다.
  1. 외부 API로부터 데이터 송수신
  2. 다양한 애플리케이션에서 비동기 통신
  3. 많은 양의 프로세스들



#### AMQP vs JMS

* AMQP : ISO 응용 계층의 MOM 표준 프로토콜
* JMS : MOM을 자바에서 지원하는 표준 API
* AMQP와 JMS는 서로 다른 것임을 기억하기!



#### 오픈 소스 메시지 큐 종류

* RabbitMQ
* ActiveMQ
* ZeroMQ
* Kafka



#### RabbitMQ

* RabbitMQ는 AMQT 프로토콜을 구현해 놓은 프로그램으로서 빠르고 쉽게 구성할 수 있으며 직관적이다.
* 거의 모든 언어와 운영체제를 지원한다.
* 신뢰성, 안정성과 성능을 충족할 수 있도록 다양한 기능을 제공한다.





#### ActiveMQ

* ActiveMQ는 자바로 만든 오픈소스 메시지 브로커입니다.
* JMS 1.1을 통해 





#### ZeroMQ

* ZeroMQ는 메시징 라이브러리입니다.
* 대부분의 AMQP들보다 확연히 빠른데, 그 이유는 다음과 같은 이유들 때문이다.
  1. AMQP에 비해 프로토콜이 단순
  2. 소켓 방식에 비해서 메시지를 보내는 방식이 단순





#### Kafka

* Kafka는 확장성과 고성능, 높은 처리량을 내세운 제품으로, 링크드인의 몇몇 개발자가 시스템 아키텍처 개선의 필요성을 느끼고 새로 개발한 시스템이다.
* 범용 메시징 시스템에서 제공하는 다양한 기능들은 Kafka에서는 찾아볼 수 없다. 분산 시스템을 기본으로 설계되었기 때문에 기존 메시징 시스템에 비해 분산 및 복제 구성을 손쉽게 할 수 있다.

* 대용량 실시간 로그 처리에 특화되어 있다.
* 노드 장애에 대한 대응성을 가지고 있다.
* 메시지를 파일시스템에 저장하기 때문에 메시지가 많이 쌓여도 성능이 크게 감소하지 않는다.
* AMQP나 JSM을 사용하지 않고 TCP 기반 프로토콜을 사용해 메시지 헤더를 이용하므로 오버헤드가 비교적 작다.



## 카프카 Kafka

#### 카프카의 기본 구조

* 프로듀서(Producer)
  + 메시지를 생산하여 브로커의 토픽으로 전달
* 브로커(Broker)
  * 카프카 애플리케이션이 설치되어 있는 서버 또는 노드
* 컨슈머(Consumer)
  * 브로커의 토픽으로부터 저장된 메시지를 전달받는 역할
* 주키퍼(Zookeeper)
  * 분산 애플리케이션 관리를 위한 코디네이션 시스템
  * 분산된 노드의 정보를 중앙에 집중하고 구성관리, 그룹 네이밍, 동기화 등의 서비스 수행
* 토픽(Topic)
  * 메시지를 논리적으로 묶은 개념 (ex. 스포츠, 인물, 지역 ...)
  * 프로듀서가 메시지를 보낼 경우 토픽에 메시지가 저장됨
* 파티션(Partition)
  * 토픽을 구성하는 데이터 저장소이자 메시지가 저장되는 위치
  * 여러개의 프로듀서에서 한 개의 파티션으로 메시지를 보낼 경우 



### 작동방식

1. 프로듀서는 새 메시지를 카프카에 전달
2. 전달된 메시지는 브로커의 토픽이라는 메시지 구분자에 저장됨
3. 컨슈머는 구독한 토픽에 접근하여 메시지를 가져옴(pull 방식)

#### 카프카가 데이터를 다루는 방식

* 분산처리
    * 또한 카프카는 갑작스러운 환경 변화에도 안정적으로 확장 가능하도록 설계되었다. 데이터의 양에 따라 브로커의 개수를 스케일 인-아웃 가능하게 할 수 있다. 
    * 상용 환경에서 카프카는 보통 최소 3개 이상의 브로커(=서버)에서 분산 운영하는데, 운영 중 일부 서버에 장애가 발생하더라도 데이터를 지속적으로 복제하기 때문에 안전하게 운영할 수 있기 때문이다. 
* 데이터 전송 방식
    * 카프카는 프로듀서와 컨슈머가 브로커와 데이터를 송수신할 때 배치로 묶음 단위로 처리한다. 이렇게 함으로써 많은 데이터를 처리하고 지연을 낮출 수 있다. 
    * kafka에 전달할 수 있는 데이터 포맷은 사실상 제한이 없다. 직렬화, 역직렬화를 통해 ByteArray로 통신하기 때문에 자바에서 선언 가능한 모든 객체를 지원한다.
    * 따라서 Producer, Consumer 애플리케이션을 실행할 때 필수 옵션으로 레코드의 메시지 키, 값을 직렬화하는 클래스를 지정한다.
    * 만약 필요할 경우 카프카에서 제공하는 커스텀 직렬화/역직렬화 클래스를 상속받아 개발하여 사용할 수도 있다.
* 카프카는 다른 MQ 플랫폼과 다르게 전송받은 데이터를 메모리에 저장하지 않고 파일 시스템에 저장한다. 운영체제에서는 파일 I/O 성능 향상을 위해 페이지 캐시 영역을 메모리에 따로 생성하여 사용한다.    

* 데이터의 저장, 전송
    * 카프카 브로커는 프로듀서로부터 데이터를 전달받으면 지정된 토픽의 파티션에 데이터를 저장하고 컨슈머가 데이터를 요청하면 파티션에 저장된 데이터를 전달한다.
    * 프로듀서로부터 전달된 데이터는 메모리나 데이터베이스에 저장되지 않고, 파일 시스템에 저장된다. 심지어 캐시메모리를 구현하여 사용하지도 않는다.
    * 대신 파일 입출력으로 인한 속도 이슈를 **페이지 캐시**를 사용하여 디스크 입출력 속도를 높여서 이 문제를 해결했다.
    * 페이지 캐시란 OS에서 파일 입출력의 성능 향상을 위해 만들어 놓은 메모리 영역이다. 한번 읽은 파일의 내용은 메모리의 페이지 캐시 영역에 저장시켜 추후 동일한 파일의 접근이 일어나면 디스크가 아닌 메모리에서 직접 읽는 방식이다.
    * 이러한 특징 때문에 가비지 컬렉션이 자주 일어나지 않아도 되며, 따라서 카프카 브로커를 실행하는데 힙 메모리 사이즈를 크게 설정할 필요가 없다.
* 데이터 복제, 싱크
    * 카프카는 데이터 복제(replication)를 통해 장애가 나더라도 전체 시스템이 원활하게 돌아갈 수 있도록 한다.

* 데이터레이크 datalake
    * 데이터레이크란 
    * 데이터 레이크 아키텍쳐의 종류는 크게 2가지가 있다.
      1. 람다 아키텍처 lambda architecture
      2. 카파 아키텍처 kappa architecture
      3. 

#### 카프카의 데이터 복제
* 카프카의 데이터 복제는 파티션 단위로 이루어진다. 토픽을 생성할 때 파티션의 복제 개수도 같이 설정되는데 직접 옵션을 선택하지 않으면 브로커에 설정된 옵션값을 따라간다.


#### 기존 메시징 시스템과 다른 점

* 디스크에 메시지 저장
  * 기존 메시징 시스템과 가장 구분되는 특징
  * 기존 메시징 시스템은 컨슈머가 메시지를 소비하면 큐에서 바로 메시지를 삭제
  * 하지만, 카프카는 컨슈머가 메시지를 소비하더라도 디스크에 메시지를 일정기간 보관하기 때문에 메시지의 손실이 없음(영속성)
* 멀티 프로듀서, 멀티 컨슈머
  * 디스크에 메시지를 저장하는 카프카의 특징 덕분에 멀티 메시징이 가능하다. 
* 분산형 스트리밍 플랫폼
  * 단일 시스템 대비 성능이 우승하며, 시스템 확장이 용이하다.
  * 내고장성(일부 노드가 죽더라도 다른 노드가 해당 일을 지속)
* 페이지 캐싱
  * 카프카는 잔여 메모리를 이용해 디스크 Read/Write를 하지 않고 페이지 캐시를 통한 Read/Write으로 인해 처리속도가 매우 빠름
* 배치 전송 처리
  * 서버와 클라이언트 사이에서 빈번하게 발생하는 메시지 통신을 하나씩 처리할 경우 그만큼 네트워크 왕복의 오버헤드 발생
  * 이로인해 메시지를 작은 단위로 묶어 배치 처리를 함으로써 속도 향상에 큰 도움을 줌





## 카프카가 필요한 상황



#### 다른 MQ들과 비교

* 공통점: 모두 비동기 통신을 제공하며, 프로듀서와 컨슈머가 분리되어 있음



#### RabbitMQ



#### Kafka

* 고성능, 고가용성, 확장성






--- 정리 필요 --


# 


## 1.1 Kafka란?

- 고성능 분산형 이벤트 스트리밍 플랫폼
- 

## 1.2 Kafka의 특징
- 분산 처리 시스템 --> 고성능, 고가용성, 확장성 --> 노드 장애 대응성 높음
- 프로듀서와 컨슈머의 분리 --> Producer 중심적
- 메시지를 디스크에 저장 --> 메시징 시스템과 같이 영구 메시지 데이터를 여러 컨슈머에게 허용
- 높은 처리량을 위한 메시지 최적화
- 데이터가 증가함에 따라 스케일아웃이 가능한 시스템 (** 스케일아웃: 장비추가 확장방식)
- 멀티 프로듀서, 멀티 컨슈머
- 잔여 메모리가 아닌 페이지 캐시를 통해 Read/Write를 하므로 처리속도가 매우 빠름
- 메시지를 작은 단위로 묶어 배치 처리를 함으로써 속도 향상에 큰 도움을 줌

## 1.3 기본구조
*카프카 클러스터
- 메시지를 저장하는 ```저장소```
- 여러 개의 브로커(= 각각의 서버)로 구성
- 브로커들이 메시지를 나눠서 저장, 이중화 처리, 장애가 나면 대체
- 데이터를 이동하는 데 필요한 핵심 역할

* 주키퍼(Zookeeper)
- 분산 애플리케이션 관리를 위한 코디네이션 시스템
- 분산된 노드의 정보를 중앙에 집중하고 구성관리, 그룹 네이밍, 동기화 등의 서비스 수행

* 주키퍼 클러스터(앙상블)


## 2.1 핵심 용어들
* 메시징 시스템
- Publisher와 Subscriber로 이루어진 비동기 메시징 전송 방식
- Message : 데이터 단위
- Producer(=Publisher) : 메시지를 보내는 측 --> 메시지 시스템에 메시지 저장
- Consumer(=Subscriber) : 데이터를 수신하는 측
- 발신자의 메시지에는 수신자가 정해져있지 않은 상태로 발행
- 구독을 신청한 수신자만이 정해진 메시지를 수신
- 수신자는 발신자 정보가 없어도 메시지 수신이 가능


* 토픽과 파티션
- Topic : 메시지를 구분하는 단위. 파일 시스템의 폴더와 유사
- Partition : 토픽을 구성하는 데이터 저장소
- 한 개의 토픽은 한 개 이상의 파티션으로 구성
- 각각의 메시지를 알맞게 구분하기 위한 목적으로 사용
- 프로듀서와 컨슈머가 토픽을 기준으로 메시지를 주고받음

* 오프셋과 메시지순서
- offset : 파티션마다 메시지가 저장되는 위치
- 파티션 내에서 시퀀셜하게 증가하는 숫자 형태 / 동일 파티션 내 메시지의 순서 보장
- 컨슈머는 메시지를 가져올 때마다 오프셋 정보를 커밋(commit) --> 현재 위치정보 저장


