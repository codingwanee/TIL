# akka-http

* akka-http는 akka module에서 분리되어 독립적인 배포 사이클을 가진다.
* akka-http는 akka-http-core와 akka-http로 되어있는데, http가 http-core를 dependency하게 되어 있으므로 http만 선언해도 된다.


## akka-http route
* akka-http route API는 DSL을 제공하며, 이는 1개 이상의 Directive로 구성되어 요청에 대한 route 처리를 기술하게 한다.
* 