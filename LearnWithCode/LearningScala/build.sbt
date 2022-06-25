name := "LearningScala"

version := "0.1"

scalaVersion := "2.12.12"


val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.9"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
) ++ Seq (
<<<<<<< HEAD:Scala/LearnWithCode/LearningScala/build.sbt
// "com.h2database" % "h2" % "1.4.197" % Test,
  "com.h2database" % "h2" % "1.4.197" ,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.29" % Test

)  ++ Seq(
    "com.typesafe.slick" %% "slick" % "3.3.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3"
=======
  "com.typesafe.akka" %% "akka-remote" % "2.6.13"
) ++ Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3"
<<<<<<< HEAD
) ++ Seq(
  // https://mvnrepository.com/artifact/org.apache.kafka/kafka
  "org.apache.kafka" %% "kafka" % "2.8.0"
=======
>>>>>>> cf49136c08a738f2884a0aa5939735e4200fc231:LearnWithCode/LearningScala/build.sbt
>>>>>>> d75da166d08732296f79b7d39efd3d2f4a2dcfc3
)



//) ++ Seq (
//  "com.typesafe.slick" %% "slick" % "3.3.3",
//  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
//  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
//  "com.h2database" % "h2" % "1.4.198" % Test
//
//)
//
