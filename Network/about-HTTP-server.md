# HTTP 서버 만들기

## 동작방식
1. TCP 소켓을 연다.
2. 서버로 들어온 HTTP 요청 메시지를 해석한다.
3. 요청한 리소스(HTML, plain text, image, 동영상 등)를 클라이언트에게 HTTP 응답 메시지로 보내준다.




## 자바로 만들어 본 간단한 HTTP 서버 예시

* main 메소드
```java
import com.java.httpServer.main.com.java.httpServer.module.HttpServerModule;

public class ServerStart {

    public static void main(String[] args) {
        System.out.println("wanee is starting HTTP Server~!!!");
        new HttpServerModule();
    }
}

```


* 서버 구현부
```java

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;


public class HttpServerModule {

    public HttpServerModule() {

        HttpServer server = null;
        Integer listenPort = 12339; // 동작시킬 포트 번호

        try {
            server = HttpServer.create(new InetSocketAddress(listenPort), 0);
            server.createContext(("/test"), new TestHandler()); // 컨텍스트명
            server.start();
        } catch(Exception e) { //httpServer 시작
            e.printStackTrace();
        }

    }


    public class TestHandler implements HttpHandler {

        public void handle(HttpExchange test) { // test 컨텍스트로 입력될 경우 처리

            String inputLine = ""; // 입력
            String resultStr = ""; // 결과

            try {
                System.out.println("*** this is test handler");

                // 요청을 받아오는 부분
                String reqMethod = test.getRequestMethod(); // 요청 메소드
                InputStream in = test.getRequestBody(); // 요청 body
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                // 응답 부분
                StringBuffer response = new StringBuffer();

                while((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                String resStr = response.toString();

                System.out.println(resStr);

                Headers h = test.getResponseHeaders();
                h.add("Content-Type", "application/json;charset=UTF-8");
                h.add("Access-Control-Allow-Origin", "*");

                test.sendResponseHeaders(200, 0);
                OutputStreamWriter osw = new OutputStreamWriter(test.getResponseBody(), "UTF-8");
                osw.write("Http response");

                try {
                    if(osw != null) osw.close();
                    test.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}


```


#### 각 부분에 대한 설명

```java
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
```

* HTTP에 대한 지원을 제공하는 기본 클래스들은 자바, 스칼라 뿐 아니라 닷넷 등 많은 언어에서도 지원한다.
* 같은 이름으로 비슷한 기능을 제공하지만, 세부적인 구현 방식은 당연히 언어에 따라 다르므로 기본 틀만 익혀보자.
* 참고한 곳  https://docs.microsoft.com/ko-kr/dotnet/api/system.web?view=netframework-4.8

#### HttpHeaders
* HttpHeaders 클래


#### httpserver.HttpServer
* HttpServer 클래스는 간단한 HTTP 서버를 구현한다.
* httpServer.HttpServer
    * httpServer - 이 주소의 클라이언트에서 들어오는 TCP 연결을 위한 IP주소, port번호에 바인딩되어 해당 주소의 클라이언트로부터 TCP 연결을 받는다.
    * HttpServer - HTTP 요청을 처리하는 서버를 구현한다.
* 요청을 처리하기 위해서는 하나 이상의 HttpHandler 객체가 서버와 연결되어 있어야 한다.


#### httpserver.HttpHandler
* HttpHandler는 해당 서버에서 애플리케이션 또는 서비스의 위치를 나타내는 루트 URI 경로로 등록된다. 
* 핸들러를 찾을 수 없는 모든 요청은 404 응답으로 거부된다.
* Executor 객체를 통해 외부에서 스레드 관리가 가능하다. 따로 제공되는 게 없으면 기본 구현이 사용된다.


#### HttpContext
* HttpContext는 데이터를 저장하고 데이터를 편리하게 주고받기 위해서 사용하는 객체다.
* HttpContext 객체는 HttpServer에 대한 핸들러 매핑을 캡슐화한다.
* 즉 개별 HTTP 요청에 대한 HTTP 관련 정보를 모두 캡슐화한다.
* HttpContext는 createContext(String, HttpHandler)를 호출하여 생성할 수 있다.


#### httpserver.HttpExchange
* 받아온 HTTP 요청을 캡슐화해서 응답을 생성하도록 한다.
* 해당 요청을 처리하고 응답을 생성, 전송하기 위한 메소드를 제공한다.
