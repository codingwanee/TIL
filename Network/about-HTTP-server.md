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

* Headers

* HttpExchange
    * 받아온 HTTP 요청을 캡슐화해서 응답을 생성하도록 한다.
    * 해당 요청을 처리하고 응답을 생성, 전송하기 위한 메소드를 제공한다.

* HttpHandler

* HttpServer