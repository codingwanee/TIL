# 액터 찾기 & 참조하기

* 참고한 글 https://hamait.tistory.com/680?category=79128


## 액터 식별
* 액터는 로컬 액터의 자식 액터를 원격에 둘 수 있다. 이렇게 원격에 있는 액터들의 위치를 알아야 하기 때문에 액터를 식별하는 것이 중요하다.

#### 액터의 위치
* 최상위의 사용자 액터들은 /user/ 아래에 위치한다.
* /user/* 하면 최상위의 모든 액터로 가는 패스를 리턴한다.
* ../* 는 현재 액터와 모든 형제들을 의미한다.


## ActorSelection
* 와일드카드를 이용하여 액터의 위치를 처리하는 방법이 필요할 때
* 원격 액터시스템의 액터와 커뮤니케이션 하고 싶은데 그 액터에 대한 참조를 아직 가지고 있지 않을 때
* actorSelection은 ActorRef 대신 ActorSelection 객체를 반환한다.(개수가 여러개일 수 있음)
* 액터 시스템에 있는 임의의 액터들과 통신하기 위해서는 actorSelection을 이용한다.
* actorSelection은 실제 액터 참조를 알려주진 않는다. 실제 액터 참조를 얻기 위해서는 Identify 메세지를 이용한다.


## Actor Ref


## Actor Path
* Actor Path 형식
    * "akka://my-sys/user/service-a/worker1"                   // 지역 액터 패스 
    * "akka.tcp://my-sys@host.example.com:5678/user/service-b" // 원격 액터 패스 


## Actor Path와 Actor Ref의 차이점
* Ref는 액터가 실제로 있어야 만들어진다. 반면 Path는 실제 액터가 없어도 만들 수 있다.
* A 액터를 만든 뒤 종료시키고 B 액터를 만들었을 때, A와 B는 동일한 Path를 가질 수도 있다. 그러나 Ref는 그대로 사용할 수 없다. Actor Ref는 대응되는 액터의 라이프 사이클과 함께 한다.



#### 유의사항
* 가능하면 항상 ActorRef를 사용할 것
* ActorPath는 액터가 죽어도 계속 메시지를 보낸다.
* ActorRef는 참조하는 액터들의 라이프 사이클을 알 수 있다.
* 개별 액터코드 내에 액터패스를 하드 코딩하지 말고 한 곳에 모아둬라.