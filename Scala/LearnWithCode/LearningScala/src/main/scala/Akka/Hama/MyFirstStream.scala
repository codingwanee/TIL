package Akka.Hama

import akka.stream.ActorMaterializer

class MyFirstStream {

  def main(args: Array[String]): Unit = {
    implicit val materializer = ActorMaterializer()

  }

}
