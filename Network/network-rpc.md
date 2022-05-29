## RPC (Remote Procedure call) 원격 프로시저 호출
* 분산 네트워크 환경에서 조금 더 편하게 프로그래밍 하기 위해 등장한 개념
* 기존 Client-Server 패턴에서 커뮤니케이션에 필요한 상세정보는 최대한 감추고, 클라이언트와 서버는 일반 메소드를 호출하는 것처럼 호출하면 된다.
* RPC의 대표적인 구현체
    * Google의 ProtocolBuffer
    * Facebook의 Thrift
    * Twitter의 Finalge

#### 중요 개념
* Caller/Callee
    * 사용자(Client/Server)가 필요한 비즈니스 로직을 작성하는 Layer
    * IDL(interface definition language)로 작성
* Stub
    * Stub Compiler가 IDL 파일을 읽어 원하는 Language로 생성
    * Parameter Object를 Message로 marshalling/unmarshalling 하는 layer
* RPC RunTime
    * Server와 Client를 Binding하는 Layer
    * 커뮤니케이션 중 발생한 에러 처리도 진행

#### 서버-클라이언트 연결 방식
* Static Binding
    * 서버 주소 Hard Coding
    * 간단하고 효율적
    * 서버 주소 변경에 약함

* Dynamic Binding
    * 주소 변경에 매우 유동적
    * 여분의 서버를 둬야 하는 단점
        * Name Server
        * Load Balancer

#### 장점
* 비즈니스 로직에 집중할 수 있음
* 다양한 언어를 가진 환경에서 쉽게 확장할 수 있음
* 쉽게 인터페이스 협업이 가능

#### 단점
* 새로운 학습 비용
* 가독성이 낮아 사람의 눈으로 읽기 힘듬

