package mission

import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}


// main문 실행시키는 부분
object ScalaHttpServer {

  def main(args: Array[String]): Unit = {

    // HttpServer 인스턴스 생성
    val server = HttpServer.create(new InetSocketAddress(8000), 0)

    //
    server.createContext("/", new RootHandler())
    server.setExecutor(null)

    // 서버 시작
    server.start()

    println("wanee started server . . .")
    println("press any key to exit")

    val key = System.in.read()
    println("you pressed >>> " + key)

    // 서버 종료
    server.stop(0) // 왜 int를 매개변수로 전달?
  }

}

// HttpHandler 상속받은 클래스
class RootHandler extends HttpHandler {
  def handle(exchange: HttpExchange): Unit = ???
}