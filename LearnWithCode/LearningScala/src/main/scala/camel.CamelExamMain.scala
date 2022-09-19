package apache.camel

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.impl.{DefaultCamelContext, SimpleRegistry}
import org.h2.jdbcx.JdbcDataSource

object CamelExamMain extends App {

  val ds = new JdbcDataSource
  ds.setURL(s"jdbc:h2:mem:h2db;mode=MySQL;DB_CLOSE_DELAY=-1")
  ds.setUser("sa")
  ds.setPassword("sa")

  val conn = ds.getConnection
  val stmt1 = conn.createStatement()

  stmt1.executeUpdate(
    """
      | create table if not exists test_msg(
      |   ID INT PRIMARY KEY,
      |   SUBJECT VARCHAR(80),
      |   BODY VARCHAR(1000),
      |   EMAIL VARCHAR(40),
      |   STATUS CHAR(1)
      | )
      |""".stripMargin)


  val data = Seq(
    (1, "Mail for Smith", "Dear Smith, \n This is a notification email", "smith@email.com", "R")
  )

  data.foreach { r =>
    val stmt = conn.prepareStatement("insert into test_msg values(?,?, ?, ?, ?)")
    stmt.setInt(1, r._1)
    stmt.setString(2, r._2)
    stmt.setString(3, r._3)
    stmt.setString(4, r._4)
    stmt.setString(5, r._5)
    stmt.executeUpdate()
  }

  conn.close()

  // DataSource 등록 -> Camel route가 db를 사용할 수 있게 함
  val registry = new SimpleRegistry
  registry.put("ds", ds)

  // camel context & routes
  val context = new DefaultCamelContext(registry) // context 등록
  context.addRoutes(new RouteBuilder() {
    override def configure(): Unit = {
      // example 1) 단순히 select만 하는 코드 >> 데이터가 변경되지 않으므로 계속 같은 데이터를 또 가져옴
      //      from("sql:select * from test_msg where status = 'R'?dataSource=ds")
      //        .to("log:apache.camel.ExampleMain")

      // example 2) 데이터 처리 후 결과에 따라 컬럼을 변경하도록 옵션 추가
      val sqlSelect = "select * from test_msg where status = 'R'"
      val markSuccess = "update test_msg set status = 'S' where id = :#id"
      val markFailure = "update test_msg set status = 'F' where id = :#id"

      from(s"sql:$sqlSelect?dataSource=ds&onConsume=$markSuccess&onConsumeFailed=$markFailure")
        .to("log:apache.camel.ExampleMain")
    }
  })

  context.start()
  Thread.sleep(10000)
  context.stop()

}
