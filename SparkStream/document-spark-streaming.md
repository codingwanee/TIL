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
