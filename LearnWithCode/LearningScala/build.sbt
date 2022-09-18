name := "LearningScala"

version := "0.1"

scalaVersion := "2.12.12"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.9"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-remote" % "2.6.13",
  "com.typesafe.akka"   %% "akka-actor-typed" % "2.6.19" % "provided",
) ++ Seq (
  "com.h2database" % "h2" % "1.4.197",
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.29" % Test,

)  ++ Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.quartz-scheduler" % "quartz" % "2.3.2",
) ++ Seq(
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
) ++ Seq(
  // https://mvnrepository.com/artifact/org.apache.kafka/kafka
  "org.apache.kafka" %% "kafka" % "2.8.0",
)

//) ++ Seq (
//  "com.typesafe.slick" %% "slick" % "3.3.3",
//  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
//  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
//  "com.h2database" % "h2" % "1.4.198" % Test
//)
