# 소켓 통신 Socket


## 소켓(socket)의 정의
* 소켓이란 네트워크 위에서 돌아가는 두 프로그램 간의 양방향 통신링크의 엔드포인트이다.
* 즉 IP와 port 넘버를 활용하여 만들어진 통신의 양 끝단이다.
* 소켓은 포트 넘버를 할당받아 TCP 레이어가 데이터의 목적지인 애플리케이션을 식별하도록 한다.
* 엔드포인트는 IP 주소와 포트넘버의 조합이다.



## 소켓(socket) 프로그래밍이란?
* Server와 Client가 특정 Port를 통해 실시간으로 양방향 통신을 하는 방식
* Client만 필요한 경우에 요청을 보낼 수 있는 HTTP 프로그래밍과 달리 Socket 프로그래밍은 Server 역시 Client로 요청을 보낼 수 있다.
* 계속 연결을 유지하는 연결지향형 방식이기 때문에 주로 실시간 통신이 필요한 경우에 사용한다.
* 소켓과 '소켓 프로그래밍'이라는 단어를 구분하자. 종종 HTTP vs socket 비교하는 글이 보이는데, HTTP도 소켓 위에서 동작되는 프로토콜이다. 다만 소켓 프로그래밍은 위의 설명대로 특징을 갖는다.

#### 소켓 프로그래밍의 특징
* Server와 Client가 계속 연결을 유지하는 양방향 프로그래밍 방식
* Server와 Client가 실시간으로 데이터를 주고받는 상황이 필요한 경우에 사용됨
* 실시간 동영상 Streaming이나 온라인 게임 등과 같은 경우에 자주 사용


## HTTP 프로그래밍
* 비교를 위해 HTTP 프로그래밍도 다시 정리해보자.
* HTTP는 TCP 위에 만들어진다. 즉 소켓을 양끝단으로 하는 TCP 레이어 위에 존재하는 프로토콜이다.
* 따라서 HTTP 프로토콜도 소켓 위에서 동작하는 통신 방식이다.
* HTTP 프로토콜은 Client의 요청이 있을 때만 서버가 응답하여 해당 정보를 전송하고 곧바로 연결을 종료하는 방식이다.
* 콘텐츠 위주의 데이터는 필요한 경우에만 Server로 접근하므로 HTTP 통신이 적합하다.

#### HTTP 프로그래밍의 특징
* Client가 요청을 보내는 경우에만 Server가 응답하는 단방향 프로그래밍 방식
* Server로부터 소켓 연결을 하고 응답을 받은 후에는 연결이 바로 종료
* 실시간 연결이 아니고, 응답이 필요한 경우에만 Server와 연결을 맺어 요청을 보내는 상황에 유용