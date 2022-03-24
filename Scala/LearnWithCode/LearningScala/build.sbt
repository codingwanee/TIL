name := "LearningScala"

version := "0.1"

scalaVersion := "2.12.12"

val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.9"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  , "com.typesafe.akka" %% "akka-stream" % AkkaVersion
  , "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion

)

