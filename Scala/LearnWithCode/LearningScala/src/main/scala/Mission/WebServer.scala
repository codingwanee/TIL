package Mission

import akka.actor.ActorSystem
import akka.http.javadsl.model.ContentTypes
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn

// 웹서버 코드 예시
object WebServer {

  def main(args: Array[String]): Unit = {

    // route 실행에 필수
    implicit val system = ActorSystem("myFirstHttpServer")
    implicit val mat = ActorMaterializer()
    // ActorMaterializer :

    // Future에 필요
    implicit val ec = system.dispatcher

    val route = path("hellow") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>wanee is building webserver</h>"))
      }
    }

    val bindFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, "localhost", 8080)

    StdIn.readLine()
    bindFuture.flatMap(serverBinding => serverBinding.unbind()).onComplete(_ => system.terminate())

  }
}