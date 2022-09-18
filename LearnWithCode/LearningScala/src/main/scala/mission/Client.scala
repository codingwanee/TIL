package mission

import akka.actor.{ Actor, ActorRef, Props }
import akka.io.{ IO, Tcp }
import akka.util.ByteString
import java.net.InetSocketAddress

object Client {
  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[Client], remote, replies)
}

class Client (remote: InetSocketAddress, listener: ActorRef) extends Actor{

  import Tcp._
  import context.system

  // 단계 1. TCP 매니저에게 Connect 메시지 보내기
  IO(Tcp) ! Connect(remote)

  def receive = {

    // 접속에 문제가 생겼을 시 CommandFailed로 멈춘다.
    case CommandFailed(_: Connect) =>
      listener ! "connect failed"
      context stop self

    // 접속 성공 시 커넥션 액터가 만들어지면서 Client 액터에게 Connected 메시지 돌려줌
    case c @ Connected(remote, local) =>
      listener ! c
      val connection = sender() // sender()는 새로 만들어진 커넥션 액터
      connection ! Register(self) // Register 메세지가 보내져야 새로운 커넥션 액터가 활성화 됨

      context become {
        case data: ByteString =>
          connection ! Write(data)
      }
  }


}
