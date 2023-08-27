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

* 9999번 포트에서 네트워크로 데이터를 수신하는 간단한 TCP 서버를 시작
``` shell
    $ nc -lk 9999
```

