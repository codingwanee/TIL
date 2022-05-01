package HamaCode

import HamaCode.ActorsHierarchy.ourSystem
import akka.actor._
import akka.event.Logging
import akka.actor.SupervisorStrategy._

import scala.io.Source
import scala.collection._
import scala.concurrent.duration._
//import org.apache.commons.io.FileUtils

/*
  분산 다운로드 예제
  - Supervisor 액터가 여러개의 worker 액터를 만들어서 각 액터에게 파일을 다운로드 받아 저장시키는 예제


 */



class Downloader extends Actor {
  def receive = {
    case DownloadManager.Download(url, dest) =>
      val content = Source.fromURL(url)
   //   FileUtils.write(new java.io.File(dest), content.mkString)
      sender ! DownloadManager.Finished(dest)
  }
}


// DownloadManager 동반객체
object DownloadManager {
  case class Download(url: String, dest: String)
  case class Finished(dest: String)
}


// DownloadManager 동반클래스
/*
  DownloadManager(Supervisor 관리액터)의 역할
  1) 워커에게 일 지시
  2) worker 개수에 비해 일거리가 많으면 일거리를 큐에 저장
  3) 놀고 있는 worker 큐에 저장
  4) 일하고 있는 worker 일과 워커를 매핑해서 저장
  5) worker 액터에 문제가 생기면 확인 후 다시 시작 / 혹은 resume 시킬 수 있음
 */
class DownloadManager(val downloadSlots: Int) extends Actor {
  // 생성자는 여기 따로 안 기입해도 되는것인가? --> 나중에 따로 한번 보기

  val log = Logging(context.system, this)
  val downloaders = mutable.Queue[ActorRef]() // worker 액터 큐
  val pendingWork = mutable.Queue[DownloadManager.Download]() // 보류중인 작업; 일거리 큐
  val workItems = mutable.Map[ActorRef, DownloadManager.Download]() // 일과 worker를 매핑해서 저장

  override def preStart(): Unit = {
    for (i <- 0 until downloadSlots) {
      downloaders.enqueue(context.actorOf(Props[Downloader], s"dl$i"))
    }
  }

  private def checkMoreDownloads(): Unit = {
    if (pendingWork.nonEmpty && downloaders.nonEmpty) {
      val dl = downloaders.dequeue()
      val workItem = pendingWork.dequeue()
      log.info(s"$workItem starting, ${downloaders.size} download slots left")
      dl ! workItem
      workItems(dl) = workItem
    }
  }

  def receive = {
    case msg @ DownloadManager.Download(url, dest) =>
      pendingWork.enqueue(msg)
      checkMoreDownloads()
    case DownloadManager.Finished(dest) =>
      workItems.remove(sender)
      downloaders.enqueue(sender)
      log.info(s"Down to '$dest' finished, ${downloaders.size} down slots left")
      checkMoreDownloads()

  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 6, withinTimeRange = 30 seconds) {
      case fnf: java.io.FileNotFoundException =>
        log.info(s"Resource could not be found: $fnf")
        workItems.remove(sender)
        downloaders.enqueue(sender)
        Resume
      case _=>
        Escalate
    }

}



object SupervisionDownloader extends App {
  import DownloadManager._
  val manager = ourSystem.actorOf(Props(classOf[DownloadManager], 4), "manager")
  manager ! Download("http://www.w3.org/Addressing/URL/url-spec.txt", "url-spec.txt")
  Thread.sleep(1000)
  manager ! Download("https://github.com/scala/scala/blob/master/README.md", "README.md")
  Thread.sleep(5000)
  ourSystem.stop(manager )
  Thread.sleep(5000)
  ourSystem.terminate()
}
