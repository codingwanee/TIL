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
// "com.h2database" % "h2" % "1.4.197" % Test,
  "com.h2database" % "h2" % "1.4.197" ,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.29" % Test

)  ++ Seq(
    "com.typesafe.slick" %% "slick" % "3.3.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3"
)



//) ++ Seq (
//  "com.typesafe.slick" %% "slick" % "3.3.3",
//  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
//  "com.typesafe.slick" %% "slick-codegen" % "3.3.3",
//  "com.h2database" % "h2" % "1.4.198" % Test
//
//)
//
