package mission

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.io.StdIn


object WebServer {



  def main(args: Array[String]): Unit = {

    // route 실행에 필수
    implicit val system = ActorSystem("myFirstHttpServer")
    implicit val mat = ActorMaterializer()

    // Future에 필요
    implicit val execute = system.dispatcher

    val route = path("wanee") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>*~* welcome to wanee server *~*</h1>"))
      }
    }

    println("wanee server is started...")

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(route)
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

    /*
     val bindFuture = Http().bindAndHandle(route, "localhost", 8080)
     println("press any key to close server...")
     StdIn.readLine()
     bindFuture
        .flatMap(serverBinding => serverBinding.unbind())
        .onComplete(_ => system.terminate())
    */

  }



}
