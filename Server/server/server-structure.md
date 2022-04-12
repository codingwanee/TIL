# about 서버 구조

## 서버 - software
    
#### 서버 OS
* 서버용 OS는 클라이언트로부터 오는 동시 다수의 접속에 응답할 수 있는 성능을 갖추고 있다.
* 현재 가장 널리 통용되는 3가지 서버 OS는 다음과 같다.
    - Windows Server
    - UNIX 계열
    - Linux


#### Windows Server
* 마이크로소프트 사에서 제공하는 서버용 OS
* 서버용 OS지만 Windows와 같은 사용자 인터페이스로 제공
* 기업이나 단체에서 필요로 하는 기능이 미리 패키지로 되어있고, MS사의 지원도 있어 사용이 용이하다.


#### UNIX 계열
* AT&T에서 개발
* 서버 제조사들이 제공


#### Linux
* 리누스 토발즈가 UNIX를 참고하여 개발
* open source, 상용으로는 Red Hat등이 제공
* 역사적 배경에서 UNIX 계열과의 친화성이 높다.
* 오픈소스기 때문에 비교적 간단하고 저렴하게 시스템 구축이 가능하다.


## PC 서버
* PC 서버는 PC와 같은 구조로 대형화 된 것 같은 서버이다.
* Intel의 x86이라는 CPU가 대표적이기 때문에 IA(Intel Architecture) 서버, x86 서버라고 불리기도 한다.
* x86 이외에 오라클의 RISC, IBM의 Power 등도 대표적이다.


## 서버 - middleware

#### 미들웨어란?
* 미들웨어는 소프트웨어를 계층적으로 표현할 때 OS와 애플리케이션 사이에서 'OS의 확장 기능이나 애플리케이션에 공통적 기능을 제공하는 역할'을 한다.
* 서버와 PC는 역할이 다르기 때문에 미들웨어에서 필요로 하는 기능은 달라진다.
* 미들웨어의 대표적인 예
    - DBMS
    - 웹 서비스