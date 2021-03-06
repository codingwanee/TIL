# Slick
* Scala Language-Integrated Connection Kit
* 공식문서 https://scala-slick.org/docs/ (버전별 문서 참고)

## Slick의 기초
* Slick은 Scala scala와 함께 쓸 수 있는 type-safe하게 DB에 CRUD 할 수 있는 query API이다.
* JDBC 연결해서 DB에 접속, 쿼리를 실행할 수 있는 기능


## 간단한 예제 코드

```scala
class Coffees(tag: Tag) extneds Table[(String, Double)](tag, "COFFEES") {
    def name = column[String]("COF_NAME")
    def price = column[Double]("PRICE")
    def * = (name, price)
}

val coffees = TableQuery[Coffees]

/* filter() 클래스의 예시 */
val q1 = coffees.filter(_.wupID === 101)
// compiles to SQL (simplified):
//   select "COF_NAME", "SUP_ID", "PRICE", "SALES", "TOTAL"
//     from "COFFEES"
//     where "SUP_ID" = 101

```
* 슬릭에서 값의 동일여부를 비교할 때는 반드시 아래 연산자를 쓴다
    * 같다   ===
    * 다르다  =!=

## Slick의 쿼리 작성법
* 참고문서 https://scala-slick.org/doc/3.1.0/queries.html

## querying(조회문)
* querying은 result() 메소드를 통해 Slick의 Action 으로 변환될 수 있다.
* Action은 직접적으로 streaming에서 실행되거나 다른 Action과 확장 혼합될 수 있다.

```scala
val q = coffees.map(_.price)
val action = q.result
val result: Future[Seq[Double]] = db.run(action)
val sql = acton.statements.head // head: 딱 한건만 조회
```
* 딱 1건만 조회하고 싶다면 Action 객체에 head나 headOption을 추가할 수 있다.

* def all = db.run(query.result)


## deleting
* deleting은 querying과 거의 비슷하게 작동한다.
* select문으로 데이터를 조회해온 뒤에 delete() 메소드로 해당 row를 삭제하는 방식이다.
* deleting 할 때는 한 번에 한 테이블만 처리할 수 있다.
* projection(select절에 특정 attribute만 선택하는 것)은 허용되지 않는다. full rows 삭제의 위험이 있기 때문이다.

```scala
val q = coffees.filter(_.supId === 15)
val action = q.delete
val affectedRowsCount: Future[Int] = db.run(action)
val sql = action.statements.head
```



## inserting
* inserting은 한 테이블에서 특정 컬럼을 선택하는 것을 기반으로 한다.
* 어떤 테이블을 직접적으로 이용하고 있다면, insert는 컬럼을 *로 선택하는 것처럼 실행된다.
* 만약 컬럼 일부를 생략하면 Database가 테이블 정의에 따라 지정된 디폴트값을 사용하거나, 디폴트값이 없는 경우 각 자료형의 디폴트값을 삽입한다.

```scala
val insertActions = DBIO.seq(
    coffees += ("Colombian", 101, 7.99, 0, 0)

    // 참고로 공식문서에는 위처럼 나와있었지만, 실제로 작성해보니
    // CoffeeTable("Colombian", 101, 7.99, 0, 0) 이런식으로
    // 객체를 한번 더 감싸서 표현해줘야 했음
    
    coffees ++= Seq(
        ("French_Roast", 49, 8.99, 0, 0),
        ("Espresso", 150, 9.99, 0, 9)
    ),
    // 아래 명시되지 않은 "sales" and "total" 컬럼은 디폴드값인 0이 삽입됨
    coffees.map(c => (c.name, c.supId, c.price)) += ("Colombian_Decaf", 101, 8.99)
)

// 특정 값이 명시되지 않은 선언문을 가져옴
val sql = coffees.insertStatement
// ==> INSERT INTO "COFFEES" ("COF_NAME","SUP_ID","PRICE","SALES","TOTAL") VALUES (?,?,?,?,?)

```

* AutoInc 컬럼이 포함되어 있으면 무시되고 database가 적당한 값을 생성한다.

```scala
val userId = (users returning users.map(_.id)) += User(None, "Stefan", "Zeiger")
```

* client가 직접 데이터를 작성해 넣는 방법 외에, database server에서 scala expression을 실행해서 데이터를 삽입할 수도 있다.
* 이 경우 AutoInc 열이 무시되지 않는다.

```scala
class Users(tag: Tag) extends Table[(Int, String)](tag, "users") {
    def id = column[Int]("id", 0.PrimaryKey)
    def name = column[String]("name")
    def * = (id, name)
}
val users = TableQuery[Users]

val actions = DBIO.seq(
    users.schema.create,
    users forceInsertQuery (users.map { u => (u.id, u.first ++ " " ++ u.last) }),
    users forceInsertExpr (users.lenth +1, "admin")
)
```




## updating
* update문은 update할 데이터를 select하고 새로운 데이터로 교체하는 방식으로 동작한다.
* 오직 한 번에 한 테이블에서 raw data로만 조회할 수 있고, 계산된 컬럼은 조회할 수 없다.
* 좀 더 다양한 메소드는 다음 문서에서 확인
    https://scala-slick.org/doc/3.0.0/api/index.html#slick.driver.JdbcActionComponent@UpdateActionExtensionMethodsImpl[T]:JdbcDriver.UpdateActionExtensionMethodsImpl[T]

```scala
val q = for { c <- coffees if c.name === "Espresso" } yield c.price
val updateAction = q.update(10.49)
val sql = q.updateStatement
```


## upserting
* upsert는 말 그대로 update + insert의 준말로, 중복되는 값이 있다면 update를 하고 없으면 insert를 하는 쿼리이다.
* 대상 객체는 반드시 해당 row와 매칭되기 위해서 테이블의 primary key를 포함해야 한다.
```scala
val updated = users.insertOrUpdate(User(Some(1), "Admin", "Zeiger"))
val updatedAdmin = (users returning users).insertOrUpdate(User(Some(1), "Slick Admin", "Zeiger"))
```


## compiled queries
* database query는 ID 등을 매개변수로 넘겨서 쿼리를 수행한다. 이렇게 파라미터로 넘긴 값들을 이용해 Query 객체를 만들면 매번 Slick의 Query를 다시 컴파일 해야 하는 비용이 발생한다.
* compiled queries은 이 불편함을 해소하기 위해 나온 개념.... --> 나중에 다시 보기!

```scala

```


#### sorting
* sortBy(_.name.desc.nullsFirst) : order by "COF_NAME" desc nulls first

#### filtering
* drop(10) : offset 10
* take(5) : limit 5


## 집합

##### applicative joins
```scala
val joinSample = for {
    (c, s) <- coffees join suppliers // coffes alias c, suppliers alias s
} yield (c.columnA, s.columnB)
```

* cross join : A join B
* inner join : A join B on (_.columnA === _.columnB)
* left outer join : A joinLeft B on (_.columA === _.columnB)
* right outer join : A joinRight B on (_.columA === _.columnB)
* full outer join : A joinFull B on (_.columA === _.columnB)


##### monadic joins
* 모나딕 조인은 flatMap으로 생성된다.
* applicative join보다 더 강력한 조인을 제공한다.


##### zip joins
* Slick은 관계형 데이터베이스에 applicative join 뿐만 아니라 zip join도 제공한다.
* zip join은 pair-wise (각 값들이 다른 파라미터의 값과 최소한 한번씩은 조합을 이룬다는 뜻) join을 제공하는 조인이다.

```scala
val jonadicJoins = for {
    (a, b) <- coffes zip suppliers
} yield (a.name, b.name)
```
* A zip B : zipWithIndex와 함께 사용된다. 0부터 시퀀셜하게 결과를 묶는다.
* A zipWith(B, (a: Atable, b: Btable) => (a.columnA, b.columnB))

```scala
val zipWithIndexJoin  = for {
    (c, idx) <- coffees.zipWithIndex
} yield (c.name, idx)
```
* zipWithIndex 

#### unions
* ++ 연산기호나 union 연산자로 두 쿼리를 합쳐 쓸 수 있다.
```scala
val q1 = coffees.filter(_.price < 8.0)
val q2 = coffees.filter(_.price > 9.0)

val unionQuery = q1 union q2
// compiles to SQL (simplified):
//   select x8."COF_NAME", x8."SUP_ID", x8."PRICE", x8."SALES", x8."TOTAL"
//     from "COFFEES" x8
//     where x8."PRICE" < 8.0
//   union select x9."COF_NAME", x9."SUP_ID", x9."PRICE", x9."SALES", x9."TOTAL"
//     from "COFFEES" x9
//     where x9."PRICE" > 9.0

val unionAllQuery = q1 ++ q2
// compiles to SQL (simplified):
//   select x8."COF_NAME", x8."SUP_ID", x8."PRICE", x8."SALES", x8."TOTAL"
//     from "COFFEES" x8
//     where x8."PRICE" < 8.0
//   union all select x9."COF_NAME", x9."SUP_ID", x9."PRICE", x9."SALES", x9."TOTAL"
//     from "COFFEES" x9
//     where x9."PRICE" > 9.0
```

#### aggregation (집계함수)
* aggregation 쿼리들은 collection이 아니라 scalar result를 반환한다.

``` scala
val q = coffees.map(_.price)
```

* q.min : select min(a."PRICE") from "TABLE" a
* q.max : select max(a."PRICE") from "TABLE" a
* q.sum : select sum(a."PRICE") from "TABLE" a
* q.avg : select avg(a."PRICE") from "TABLE" a

* q.length
* q.exists


```scala
val q = (for {
    c <- coffees
    s <- c.,supplier
} yield (c, s)).groupBy(_._1.supID)
```

* groupBy





## DBIOAction







## 예제 코드

#### 테이블 구조 작성
```scala
    package models

    import slick.driver.PostgresDriver.simple._

    case class User (
        id: String,
        name: String,
        email: String
    )

    object Users extends Table[User]("users") {
        def id = column[String]("id")
        def name = column[String]("name")
        def email = column[String]("email")
        def * = id ~ name ~ email <> (User, User.unapply _) // 모델 객체를 매핑해주는 함수

        def add(user: User)(implicit session: Session) = { // 함수마다 implicit 으로 Session 파라미터가 필요하다.
            Users.insert(user)
        }

        def findAll()(implicit session: Session) = {
            (for {
                user <- Users
            } yield user).list
        }

    }

```

#### 테스트케이스 작성

```scala
    package models

    import org.scalatest.FunSpec
    import org.scalatest.matchers.ShouldMatchers
    import scala.slick.driver.H2Driver.simple._
    import Database.threadLocalSession

    class UserSpec extends FunSpec with ShouldMatchers {

        describe("example") {
            it("사용자를 추가하고 조회한다") {
                Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
                    
                    // given
                    Users.ddl.create
                    Users.add(new User("outsider", "Outsider", "abc@gmail.com"))

                    // when
                    val results = Users.findAll

                    // then
                    results.size should equal(1)
                }
            }
        }
    }
```


