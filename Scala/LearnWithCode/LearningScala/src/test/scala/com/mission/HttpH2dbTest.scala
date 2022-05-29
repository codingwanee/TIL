//package com.mission
//
//import java.sql.{DriverManager, Statement}
//
//import akka.testkit.TestKit
//
////import akka.actor.ActorSystem
////import akka.http.scaladsl.model.headers.Connection
////import akka.testkit.TestKit
////import org.scalatest.BeforeAndAfterAll
////import org.scalatest.flatspec.AnyFlatSpecLike
////import org.scalatest.matchers.must.Matchers
////import org.h2.Driver
//
//import akka.http.scaladsl.model.StatusCodes
//import akka.http.scaladsl.testkit.ScalatestRouteTest
//import akka.http.scaladsl.server._
//import Directives._
//import org.scalatest.matchers.should.Matchers
//import org.scalatest.wordspec.AnyWordSpec
//
//class FullTestKitExampleSpec extends AnyWordSpec with Matchers with ScalatestRouteTest {
//
//class HttpH2dbTest extends TestKit(ActorSystem("test")) with AnyFlatSpecLike with BeforeAndAfterAll with Matchers {
//
//  val PARENT_DIR:String = "./data-dir"
//  val DATABASE_NAME: String = "wanee-h2-db" // it's better if you write db name in small letters
//  val DATABASE_DIR: String = s"$PARENT_DIR/$DATABASE_NAME" // FYI, this is string interpolation
//  val DATABASE_URL: String = s"jdbc:h2:$DATABASE_DIR"
//
//  override def beforeAll(): Unit = {
//    println("starting")
//
//  }
//
//  override def afterAll() {
//    // clean-up code
//    // ...
//  }
//
//
////  "test H2 + HTTP" must "잘 돌아간다" in {
////    var row1InsertionCheck = false
////    val con: Connection = DriverManager.getConnection(DATABASE_URL)
////    val stm: Statement = con.createStatement
////    val sql: String =
////      """
////        |create table test_table1(ID INT PRIMARY KEY,NAME VARCHAR(500));
////        |insert into test_table1 values (1,'A');""".stripMargin
////
////    stm.execute(sql)
////    val rs = stm.executeQuery("select * from test_table1")
////
////    rs.next
////    row1InsertionCheck = (1 == rs.getInt("ID")) && ("A" == rs.getString("NAME"))
////
////    assert(row1InsertionCheck, "Data not inserted")
////  }
//
//  val smallRoute =
//    get {
//      concat(
//        pathSingleSlash {
//          complete {
//            "Captain on the bridge!"
//          }
//        },
//        path("ping") {
//          complete("PONG!")
//        }
//      )
//    }
//
//  "The service" should {
//
//    "return a greeting for GET requests to the root path" in {
//      // tests:
//      Get() ~> smallRoute ~> check {
//        responseAs[String] shouldEqual "Captain on the bridge!"
//      }
//    }
//
//    "return a 'PONG!' response for GET requests to /ping" in {
//      // tests:
//      Get("/ping") ~> smallRoute ~> check {
//        responseAs[String] shouldEqual "PONG!"
//      }
//    }
//
//    "leave GET requests to other paths unhandled" in {
//      // tests:
//      Get("/kermit") ~> smallRoute ~> check {
//        handled shouldBe false
//      }
//    }
//
//    "return a MethodNotAllowed error for PUT requests to the root path" in {
//      // tests:
//      Put() ~> Route.seal(smallRoute) ~> check {
//        status shouldEqual StatusCodes.MethodNotAllowed
//        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
//      }
//    }
//  }
//}
