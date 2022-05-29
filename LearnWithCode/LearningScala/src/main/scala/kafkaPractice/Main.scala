package kafkaPractice

import java.util.Properties
import java.util.concurrent.Executors

object Main extends App{

  val threadCount = 2
  val topic: String = "test-topic"
  val consumerId: String = "test-consumer"
  val properties = new Properties()

  properties.put("group.id", consumerId)
  properties.put("bootstrap.servers", "localhost:9092")
  properties.put("key.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
  properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")

  val executor = Executors.newCachedThreadPool()

  for (threadNo <- 0 until threadCount) {
    executor.submit(new ConsumerWorker(properties, topic, threadNo))
  }

  sys.ShutdownHookThread {
    ConsumerStatus.isRunning = false

    while (ConsumerStatus.consumerInfo.nonEmpty) {
      Thread.sleep(1000)
    }

    println("Shutdown application")
  }
}
