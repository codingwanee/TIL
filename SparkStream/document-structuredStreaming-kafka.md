# Structured Streaming + Kafka Integration Guide (Kafka broker version 0.10.0 or higher)
- 구조적 스트리밍(Structured Streaming)을 이용해 카프카로부터 데이터를 읽고 쓰는 방법에 대한 도큐먼트를 번역한 글
- 공부를 위해 번역 및 정리한 글이므로 오역과 의역이 있을 수 있습니다.
- 출처: https://spark.apache.org/docs/3.1.3/structured-streaming-kafka-integration.html


## Linking
- SBT/Maven 프로젝트 정의를 사용하는 Scala/Java 어플리케이션의 경우, 어플리케이션을 아래 아티팩트에 연결하라.
- ``` yaml
    groupId = org.apache.spark
    artifactId = spark-sql-kafka-0-10_2.12
    version = 3.1.3
  ```
- 헤더 기능을 사용하려면 Kafka 클라이언트 버전이 0.11.0.0 이상이어야 한다는 것을 기억하라.
- spark-shell을 이용하고 있을 때도 위 라이브러리를 사용해야 한다. 아래 Deploying 섹션에서 설명하고 있다.

## Reading Data from Kafka
#### Creating a Kafka Source for Streaming Queries
- ``` java
    // Subscribe to 1 topic
    Dataset<Row> df = spark
    .readStream()
    .format("kafka")
    .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
    .option("subscribe", "topic1")
    .load();
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

    // Subscribe to 1 topic, with headers
    Dataset<Row> df = spark
    .readStream()
    .format("kafka")
    .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
    .option("subscribe", "topic1")
    .option("includeHeaders", "true")
    .load()
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)", "headers");

    // Subscribe to multiple topics
    Dataset<Row> df = spark
    .readStream()
    .format("kafka")
    .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
    .option("subscribe", "topic1,topic2")
    .load();
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

    // Subscribe to a pattern
    Dataset<Row> df = spark
    .readStream()
    .format("kafka")
    .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
    .option("subscribePattern", "topic.*")
    .load();
    df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");
  ```

  #### Creating a Kafka Source for Batch Queries
  - 배치 프로세스에 더 알맞은 케이스에서는 정의된 범위의 오프셋에 대한 Dataset/DataFrame을 생성할 수 있다.
  - ``` java
        // Subscribe to 1 topic defaults to the earliest and latest offsets
        Dataset<Row> df = spark
        .read()
        .format("kafka")
        .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
        .option("subscribe", "topic1")
        .load();
        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

        // Subscribe to multiple topics, specifying explicit Kafka offsets
        Dataset<Row> df = spark
        .read()
        .format("kafka")
        .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
        .option("subscribe", "topic1,topic2")
        .option("startingOffsets", "{\"topic1\":{\"0\":23,\"1\":-2},\"topic2\":{\"0\":-2}}")
        .option("endingOffsets", "{\"topic1\":{\"0\":50,\"1\":-1},\"topic2\":{\"0\":-1}}")
        .load();
        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");

        // Subscribe to a pattern, at the earliest and latest offsets
        Dataset<Row> df = spark
        .read()
        .format("kafka")
        .option("kafka.bootstrap.servers", "host1:port1,host2:port2")
        .option("subscribePattern", "topic.*")
        .option("startingOffsets", "earliest")
        .option("endingOffsets", "latest")
        .load();
        df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)");
    ```
- 소스에서 각 row는 다음 스키마를 따른다.
- |Column|Type|
|-----|----|
|key | binary |
|value | binary |
|topic | string |
|partition | int |
|offset | long |
|timestamp | timestamp |
|timestampType | int |
|headers (optional) | array |

- 
