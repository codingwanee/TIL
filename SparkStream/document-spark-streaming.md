# Spark Streaming Programming Guide 3.1.3
- Spark Streaming 3.1.3 공식 도큐멘트를 번역
- 개인 공부를 위해 정리한 내용이므로, 의역 또는 순서의 변형이 있을 수 있음.
- 출처: https://spark.apache.org/docs/3.1.3/streaming-programming-guide.html

## Overview
- Spark Streaming은 실시간 데이터 스트림에 대해 확장성, 고기능성, 장애 허용성을 제공하는 core Spark API 확장 라이브러리다.
- Kafka, Kinesis, TCP sockets 또는 map, reduce, join, window 등의 고차원 함수 등 다양한 데이터 소스를 활용할 수 있다.
- 처리가 완료된 데이터는 파일시스템, 데이터베이스, 실시간 대시보드 등으로 결과를 보낼 수 있다.
- Spark의 머신러닝과 그래프 프로세싱 알고리즘을 데이터 스트림에 이용할 수 있다.
- 이 가이드는 DStream을 사용하여 Spark Streaming 프로그램을 작성하는 방법을 보여준다.
- 이 가이드에서는 Scala, Java 또는 Python(Spark 1.2에서부터 도입됨)으로 Spark Streaming 프로그램을 작성할 수 있도록 한다.

#### Spark Streaming의 내부 동작과정
1. 내부적으로 Spark Streaming은 실시간 인풋 데이터를 받은 후
2. 데이터를 배치 단위로 쪼개고
3. Spark Engine에서 데이터 처리를 완료한 후
4. 최종 데이터 스트림 배치가 생성된다.

#### DStream
- Spark Streaming은 discretized stream 또는 DStream이라고 하는 고수준 추상화를 제공한다.
- DStream은 Kafka, Kinesis와 같은 소스로부터 입력 데이터 스트림이나 다른 DStream에 대해 고수준 작업을 적용함으로써 생성될 수 있다.
- 내부적으로 DStream은 RDD의 시퀀스로 표현된다.

## A Quick Example
- 본격적으로 Spark Streaming 프로그램을 알아보기 전에, Spark Streaming이 얼마나 심플하게 되어있는지 알아보도록 하자.
- TCP socket으로 입력받고 있는 데이터 서버에 인입된 텍스트 데이터의 단어 수를 세는 기능을 만들어보자.

1. JavaStreamingContext 객체 만들기
    - 먼저 **JavaStreamingContext** 객체를 생성하자. 이 객체는 모든 스트리밍 기능의 주요 진입점이 된다.
    - 로컬 StreamingContext를 생성하여 두 개의 실행 스레드와 1초의 배치 간격을 갖도록 만들어 보자.
    - JavaStreamingContext 객체 부연설명
        - JavaStreamingContext 객체는 Spark Streaming의 핵심 개념 중 하나로, 스트리밍 작업을 시작하고 관리하기 위해 사용된다.
        - 이 객체는 Spark Streaming의 핵심 개념 중 하나로, 스트리밍 작업을 시작하고 관리하기 위해 사용된다.
        - 위에서 언급된 두 개의 실행 스레드는 스파크 스트리밍 작업을 병렬로 실행하기 위한 것이며, 배치 간격은 데이터를 얼마나 자주 처리할 것인지를 지정하는 값이다.
        - 1초의 배치 간격을 사용하면 매 1초마다 스트리밍 데이터가 처리된다.
    - ``` java
        import org.apache.spark.*;
        import org.apache.spark.api.java.function.*;
        import org.apache.spark.streaming.*;
        import org.apache.spark.streaming.api.java.*;
        import scala.Tuple2;

        // Create a local StreamingContext with two working thread and batch interval of 1 second
        SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
      ```
      
2. DStream 생성
    - 위에서 만든 context를 이용하여 TCP 소스로부터 들어오는 데이터를 표현하는 DStream을 생성할 수 있다.
    - 이 때 hostname과 port를 지정해주도록 한다.
    - DStream 부연설명
        - Spark Streaming에서 스트림 처리를 위한 고수준의 추상화
        - 연속적인 데이터 스트림을 나타내며, 각 시간 단위(eg. seconds)마다 RDD의 시퀀스로 구성된 스트림
        - 데이터를 미니 배치로 나누어 병렬 처리하면서 실시간 처리를 가능하도록 함
    - ``` java
        // Create a DStream that will connect to hostname:port, like localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
      ```
    
3. 입력받은 데이터를 공백을 기준으로 문장을 단어로 나누기
    - 위에서 선언한 `lines` DStream은 데이터 서버로부터 받아 온 데이터 스트림을 표현한다.
    - 이 스트림 속 record 하나하나는 텍스트 라인으로 이루어진다. 이 텍스트 라인을 공백을 기준으로 각 단어로 나누어보자.
    - flatMap은 입력받은 소스 DStream 속 각 record로부터 새로운 record들로 구성된 새로운 DStream을 생성하는 기능이다.
    - 이 예시에서는 각 문장을 여러 단어들로 쪼개어 `words` DStream으로 표현할 것이다.
    - 이 flatMap은 `FlatMapFunction` 객체를 이용해 변환이 정의되어 있는 것을 기억해야 한다.
    - 뒤에서도 알게 되겠지만, Java API는 이와 같이 DStream 변환을 정의하는 데 도움이 되는 클래스들을 여러 개 제공하고 있다.
    - ``` java
        // Split each line into words
        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(x.split(" ")).iterator());
      ```
    
4. 단어 개수 세기
    - 아래 코드에서 `words` DStream 속 각 단어는 한 번에 한 단어씩 변환되어 `PairFunction` 객체를 사용해 `(단어, 1)` 쌍의 DStream으로 매핑된다.
    - Function2 객체를 사용하여 생성된 (단어, 1) 쌍의 DStream을 줄여 각 단어의 빈도를 배치 단위로 계산한다.
    - 마지막으로 `wordCounts.print()`는 매 초마다 생성된 카운트 중 일부를 출력한다.
    - ``` java
        // Count each word in each batch
        JavaPairDStream<String, Integer> pairs = words.mapToPair(s -> new Tuple2<>(s, 1));
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey((i1, i2) -> i1 + i2);

        // Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print();
      ```

5. 실행
    - 위 코드들이 실행된 후에, Spark Streaming은 수행할 계산을 설정만 하고 실제 처리를 시작하지는 않는다.
    - 실제 처리를 시작하려면 아래와 같이 `start` 메서드를 호출해야 한다.
    - ``` java
        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
      ```

6. 테스트
    - 터미널을 열어 9999번 포트에서 네트워크로 데이터를 수신하는 간단한 TCP 서버를 시작
    - ``` shell
        $ nc -lk 9999
      ```
    - `JavaNetworkWordCount` 스트리밍 어플리케이션 구동
    - ``` sh
        ./bin/run-example streaming.JavaNetworkWordCount localhost 9999
      ```

## Basic Concepts
- 다음으로, 간단한 예제를 넘어 Spark Streaming의 기본 사항에 대해 자세히 설명하겠다.

#### Linking
- Spark와 유사하게 Spark Streaming은 Maven Central에서 작동할 수 있다.
- 자신만의 Spark Streaming 프로그램을 개발할 때, SBT 또는 Maven project에 아래 의존성을 추가해야 한다.
- ``` xml
    // Maven
    <dependency>
        <groupId>org.apache.spark</groupId>
        <artifactId>spark-streaming_2.12</artifactId>
        <version>3.1.3</version>
        <scope>provided</scope>
    </dependency>
  ```
- ``` s
    // sbt
    libraryDependencies += "org.apache.spark" % "spark-streaming_2.12" % "3.1.3" % "provided"
  ```
- Spark Streaming core API에서 지원하지 않는 Kafka, Kinesis와 같은 소스에서 데이터를 받아오기 위해서는 종속성에 `spark-streaming-xyz_2.12`를 추가해야 한다.
- |Source|Artifact|
  |---|---|
  |Kafka| spark-straming-kafka-0-10_2.12|
  | Kinesis| spark-streaming-kinesis-asl_2.12[Amazon Software License]|

- [Maven repository](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.apache.spark%22%20AND%20v%3A%223.1.3%22)에서 필요한 모든 목록의 최신 버전을 확인할 수 있다.

#### Initializing StreamingContext
- Spark Streaming 프로그램을 초기화 하기 위해서는 모든 Spark Streaming 기능의 주요 진입점인 `StreamingContext` 객체를 생성해야 한다.
- `JavaStreamingContext` 객체는 `SparkConf` 객체에서 생성된다.
- ``` java
    import org.apache.spark.*;
    import org.apache.spark.streaming.api.java.*;

    SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
    JavaStreamingContext ssc = new JavaStreamingContext(conf, new Duration(1000));
  ```
    - `appName` 파라미터는 cluster UI에 보이는 당신의 어플리케이션 이름이다.
    - `master`는 Spark, Mesos or YARN cluster URL 또는 로컬 모드에서 돌아가는 특별한 문자열 `local[*]` 이다.
    - 클러스터에서 구동할 때는 `master`를 하드코딩 하지 말고 spark-submit으로 구동[Launching Applications with spark-submit](https://spark.apache.org/docs/3.1.3/submitting-applications.html)하는 게 좋을 것이다.
    - 



- context가 정의한 후 다음을 수행해야 한다.
    1. 입력 DStream을 생성하여 입력 소스를 정의
    2. DStream에 변환 및 출력 작업을 적용하여 스트리밍 연산을 정의
    3. streamingContext.start()를 사용하여 데이터 수신 및 처리 시작
    4. streamingContext.awaitTermination()을 사용하여 처리가 수동으로 중지되거나 오류로 중지될 때까지 기다림
    5. streamingContext.stop()을 사용해 처리를 수동으로 중지 가능


#### 기억할 포인트
- 일단 context가 한 번 시작되면, 새로운 스트리밍 연산이 추가될 수 없다.
- 일단 context가 한 번 멈추면, 재시작할 수 없다.
- JVM 내에서 한 번에 오직 하나의 StreamingContext만 실행될 수 있다.
- StreamingContext에서 stop()은 SparkContext도 함께 정지시킨다. StreamingContext만 정지시키기 위해서는 stop()의 인자로 `stopSparkContext`를 false로 설정해주면 된다.
- SparkContext를 종료시키지 않은 채로 이전 StreamingContext가 종료되고 다음 StreamingContext가 생성되기 전 시점이라면, SparkContext는 재사용되어 여러 개의 StreamingContext를 생성할 수 있다.


## Discretized Streams (DStreams)
- **DStream**은 Spark Streaming이 제공하는 기본적인 추상화 개념이다.
- DStream은 지속적인 데이터 스트림을 나타내는데, 이 스트림은 데이터 소스에서 입력받은 데이터 스트림일 수도 있고 이것을 변환하고 처리하여 생성된 데이터 스트림일 수도 있다.
- 내부적으로 DStream은 RDD의 연속적인 시리즈로 표현된다. DSteam의 각 RDD에는 특정 간격의 데이터가 포함되어 있다.
- DStream에 적용되는 모든 작업은 기본 RDD에 대한 작업으로 변환된다.
    - 예를 들어, 위에서 라인 스트림을 단어로 변환하는 예제에서, flatMap 작업은 Lines DStream의 각 RDD에 적용되어 단어 DStream의 RDD를 생성한다.
- Spark 엔진은 이렇게 변환된 RDD들을 연산한다. DStream 기능들은 세부적인 내용은 대부분 숨기고 개발자에게 고수준 API만 제공하여 편리하게 해준다.


## Input DStreams and Receivers
- 입력 DStream은 스트림 소스에서 입력받은 입력 데이터 스트림을 나타낸다.
- 위에서 봤던 예제에서, lines는 netcat 서버에서 입력받은 데이터 스트림을 나타내는 입력 DStream이었다.
- 파일 스트림을 제외한(추후 따로 다루기로 한다) 모든 입력 DStream은 **Receiver** ([Scala doc](https://spark.apache.org/docs/latest/api/scala/org/apache/spark/streaming/receiver/Receiver.html), [Java Doc](https://spark.apache.org/docs/latest/api/java/org/apache/spark/streaming/receiver/Receiver.html)) 객체와 연결된다. Receiver 객체는 데이터를 받아와 Spark의 메모리에 쌓는 역할을 한다.
- Spark STreaming은 2가지 종류의 빌트인 스트리밍 소스를 제공한다.
    - *Basic sources*: StreamingContext API에서 직접적으로 쓸 수 있는 소스들. ex. 파일시스템, 소켓연결
    - *Advanced sources*: Kafka, Kinesis 등등의 소스들은 추가적인 클래스를 통해 연결될 수 있다. 후에 linking 섹션에서 다루는 내용이다.
- 주의할 것은, 스트리밍 애플리케이션에 동시에 여러 개의 스트림을 병렬로 받고자 한다면 여러 개의 입력 DStream을 생성하면 된다.
- 이렇게 하면 여러 개의 receiver가 생성되어 동시에 여러 데이터 스트림을 입력받을 수 있다.
- 하지만 Spark worker/executor는 보통 장기 실행작업이므로 Spark Streaming 애플리케이션에 할당된 코어 중 하나를 차지한다.
- 따라서 수신된 데이터를 처리하고 수신기를 실행하려면 Spark Streaming 애플리케이션에 충분한 코어를 할당해야 한다는 점을 기억해야 한다.(로컬로 실행되는 경우에는 스레드)

#### 기억할 포인트
- 로컬 환경에서 Spark Streaming을 실행한다면 master URL에 "local" 또는 "local[1]"을 사용하면 안 된다. 이 두 가지는 로컬에서 딱 하나의 스레드만 사용될 것이라는 뜻이 된다.
- 만약 이 상황에서 receiver 기반의 입력 DStream을 이용한다면 receiver를 구동하는 데 싱글 스레드가 사용되고, 입력받은 데이터를 처리하는데 사용될 스레드가 없게 된다.
- 따라서 로컬 환경에서 실행할 때는 "local[n]"을 master URL에 넣고, 이 때의 *n*은 실행하려 하는 receiver보다 큰 숫자로 지정해야 한다.
- 클러스터 환경에서 실행할 때로 확장해서 보면, Spark Streaming 어플리케이션은 사용하려는 receiver의 숫자보다 더 많은 core를 할당하고 있어야 한다. 그렇지 않으면 시스템이 데이터를 입력받는 데 까지는 가능하지만 처리할 수 없게 된다.



#### Basic Sources
- 위에 quick example에서 TCP socket connection에서 텍스트 데이터를 입력받아 DStream을 생성하는 ssc.socketTextStream(...)을 살펴보았다.
- socket 외에도 StreamingContext API는 파일을 입력소스로 받아 DStream을 생성하는 메소드들을 제공한다.

#### File Streams
- HDFS API와 호화노디는 모든 파일 시스템(즉 HDFS, S3, NFS 등)의 파일에서 데이터를 읽어오려면 `StreamingContext.fileStream[KeyClass, ValueClass, InputFormatClass]`를 통해 DStream을 생성할 수 있다.
    - ``` java
        streamingContext.fileStream<KeyClass, ValueClass, InputFormatClass>(dataDirectory);
      ```
- 파일 스트림은 receiver를 필요로 하지 않기 때문에, 파일 데이터를 받아오기 위한 core를 할당해줄 필요가 없다.
- 간단한 텍스트 파일을 처리하기 위해 사용할 수 있는 가장 쉬운 메소드는 `StreamingContext.textFileStream(dataDirectory)`이다.
    - ``` java
        streamingContext.textFileStream(dataDirectory);
      ```

#### How Directories are Monitored
- Spark Streaming은 `dataDirectory` 디렉토리를 모니터링하여 해당 디렉토리에 생성된 모든 파일을 처리한다.
    - 


