package version3

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.server.{Directives, Route}
import slick.jdbc.H2Profile.api._
import slick.lifted.Tag
import spray.json._
import version2.PFriends

import scala.concurrent.Future
import scala.io.StdIn

//#imports
import scala.collection.mutable.ArrayBuffer

final case class SimpleFriends(name: String, age: Int, hobby: String)
final case class ArrayFriends(friends: List[SimpleFriends])

// collect your json format instances into a support trait:
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val friendFormat = jsonFormat3(SimpleFriends)
  implicit val arrFriendFormat = jsonFormat1(ArrayFriends)
}


object HttpWebServer3 extends Directives with JsonSupport {

  def main(args: Array[String]): Unit = {

    // 액터시스템
    implicit val system = ActorSystem(Behaviors.empty, "my-system")

    // executionContext - needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.executionContext

    val lines = new ArrayBuffer[Any]()

    def println(s: Any) = lines += s

    // H2DB
    val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    // 핸들러
    val handler = new WaneeHandler(db)


    /**
     * Http Route
     */
    //////////////////////////////   기본 HTTP 메소드   //////////////////////////////////

    // 서버 동작하는지 간단 테스트용
    val testRoute =
      get {
        complete("This is a GET request.")
      } ~ post {
        complete("This is a POST request.")
      } ~ put {
        complete("This is a PUT request.")
      } ~ delete {
        complete("This is a DELETE request.")
      }


    // json array 테스트
    val arrayRoute =
      post {
        (path("array") & entity(as[ArrayFriends])) { param =>
          complete(handler.postArray(db, param))
        }
      }

    val csvRoute = {
      path("csv") {
        (post & entity(as[String])) { param =>
          val str = param.toString
          complete(handler.postCSV(db, str))
        }
      }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // 서버 시동
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(csvRoute) // 원하는 route의 변수명으로 실행

    println(s"Server now online. Please navigate to http://localhost:8080/hello\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
