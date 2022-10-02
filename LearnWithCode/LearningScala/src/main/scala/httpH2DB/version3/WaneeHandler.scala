package version3

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import slick.dbio.Effect
import slick.jdbc.H2Profile.api._
import version1.ExtendsAppObject.Friends

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContext, Future}

// Controller에 호출되어 서비스 실행
class WaneeHandler(db: Database) extends Directives {

  // 테이블
  val friends = TableQuery[Friends]

  // 테이블 스키마, 샘플데이터 생성
  val setup = DBIO.seq(
    // 테이블 스키마
    friends.schema.create,

    // 샘플데이터
    friends += ("Wanee", 32, "riding bike"),
    friends += ("Fanee", 32, "driving"),
    friends += ("BigGuy", 32, "horse riding")
  )

  // 실행
  val setupFuture = db.run(setup)


  /**
   *  Post
   */

  def postArray(db: Database, param: ArrayFriends)(implicit ec: ExecutionContext): Future[HttpResponse] = Future {

    val f = param.friends

    for( i <- f )
    {
      val q = for { a <- friends if a.name === i.name } yield (a.age, a.hobby)
      val action = q.update(111, "성공적으로 업데이트")

      db.run(action)
    }

    // update 반영된 결과 select
    val sq = friends.map(_.*)
    val saction = sq.result
    val results: Future[Seq[(String, Int, String)]] = db.run(saction)
    results.foreach(println)

    val waitedResult = Await.result(results, 10.seconds)
    val resultStr = waitedResult.toString

    HttpResponse(
      StatusCodes.OK, // status code
      Nil, // headers
      HttpEntity(ContentType.WithCharset(MediaTypes.`text/plain`, HttpCharsets.`UTF-8`), resultStr) // entity
      //, HttpProtocols.`HTTP/1.1` // protocol
    )
  }

  def postCSV(db: Database, param: String)(implicit ec: ExecutionContext): Future[HttpResponse] = Future {

    val del1 = "\n" // 개행
    val del2 = ","  //

    // 구분자로 이중배열
    val arr1 = param.split(del1)
    val len = arr1.length
    val arr2 = Array.ofDim[String](len, 3) // 한 줄에 3개 이상의 parameter가 들어와도 오류 없이 3개까지만 들어감

    /*
    // 길이가 미정인 이중배열 선언
    val row = 5
    val column = 3
    val temp = Array.ofDim[String](row, column)
     */


    var action: DBIOAction[Unit, NoStream, Effect.Write] = DBIO.seq()


    for (i<- 0 until len) {
      arr2(i) = arr1(i).split(del2)

      for ( j <- 0 to 2) {
        // 앞뒤 공백 자르기
        arr2(i)(j) = arr2(i)(j).trim

        println(arr2(i)(j))
      }
      val name  = arr2(i)(0)
      val age   = arr2(i)(1)
      val hobby = arr2(i)(2)

      action = DBIO.seq(
        friends += (name, age.toInt, hobby)
      )

      db.run(action)
    }


    // update 반영된 결과 select
    val sq = friends.map(_.*)
    val saction = sq.result
    val results: Future[Seq[(String, Int, String)]] = db.run(saction)
    results.foreach(println)
    val waitedResult = Await.result(results, 10.seconds)
    val resultStr = waitedResult.toString

    HttpResponse(
      StatusCodes.OK, // status code
      Nil, // headers
      HttpEntity(ContentType.WithCharset(MediaTypes.`text/plain`, HttpCharsets.`UTF-8`), resultStr) // entity
      //, HttpProtocols.`HTTP/1.1` // protocol
    )
  }

}

