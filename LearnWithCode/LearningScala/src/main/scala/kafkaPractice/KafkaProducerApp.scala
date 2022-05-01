package kafkaPractice

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties


object KafkaProducerApp extends App {

  val props:Properties = new Properties()


  // 프로듀서 애플리케이션을 실행할 때 설정해야 하는 주요 옵션들이 있다.
  // 필수 옵션은 사용자가 반드시 설정해야 하는 옵션이고, 선택 옵션은 필수는 아니지만 중요하지 않은 건 아니다.
  ///// 필수 옵션 ///////////////////////////////////////////////////////////////////////////////////////////////
  props.put("bootstrap.servers", "localhost:9092") // 프로듀서가 데이터를 전송할 대상 카프카 클러스터에 속한 브로커의 호스트이름:포트
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer") // 레코드의 메시지 키를 직렬화하는 클래스 지정
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer") // 레코드의 메시지 값을 직렬화하는 클래스 지정

  ///// 선택 옵션 ///////////////////////////////////////////////////////////////////////////////////////////////
  props.put("acks","all") // 프로듀서가 전송한 데이터가 브로커들에 정상적으로 저장되었는지 전송 성공 여부를 확인하는 데 사용하는 옵션
  // buffer.memory : 브로커로 전송할 데이터를 배치로 모으기 위해 설정할 버퍼 메모리양 지정.
  // retries : 프로듀서가 브로커로부터 에러를 받고 난 뒤 재전송을 시도하는 횟수 지정.
  // batch.size : 배치로 전송할 레코드 최대 용량 지정.
  // linger.ms : 배치를 전송하기 전까지 기다리는 최소시간
  // partitioner.class : 레코드를 파티션에 전송할 때 적용하는 파티셔너 클래스

  val producer = new KafkaProducer[String, String](props)
  val topic = "test"
  try {
    for (i <- 0 to 5) {
      val record = new ProducerRecord[String, String](topic, i.toString, "I am wanee " + i)

      val metadata = producer.send(record)
      println("*~*~*~* printing log part *~*~*~*")

    }

  } catch {
    case e:Exception => e.printStackTrace()
  } finally {
    producer.close()
  }

}

