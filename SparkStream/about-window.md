# 스트림 데이터의 Window

## Kafka Stream의 Window 방식
- Kafka Streams에서는 시간 기반 윈도우 및 세션 윈도우와 같은 여러 유형의 윈도우를 지원한다.
    - Thumbling Window → Timewindow(클래스)
    - Hopping Window → Timewindow(클래스)
    - Session Window → Sessionwindow(클래스)
    - Sliding join Window → JoinWindow(클래스)
    - Sliding Aggregation Window → SlidingWindow(클래스)
- 각 종류별 특징
    1. 시간 기반 윈도우(Time-based Window)
        - 시간 간격에 따라 데이터를 그룹화
    2. 세션 윈도우(Session Window)
        - 일련의 관련된 이벤트를 하나의 세션으로 그룹화

## Spark Streaming의 Window 방식
- 각 종류별 특징
    1. 시간 기반 윈도우(Time-based Window)
        - Spark Streaming은 일정한 시간 간격으로 데이터를 그룹화하고 처리
    2. 슬라이딩 윈도우(Sliding Window)
        - 시간 기반 윈도우와 유사하지만, 윈도우가 겹치게 동작
        - 새로운 데이터 포인트가 윈도우를 벗어나면 해당 데이터 포인트가 이전 윈도우에서 제거되고 새로운 윈도우에 추가

# 슬라이딩 윈도우에서 데이터 중복 처리를 막는 현상
- Spark Streaming에서도 Kafka Stream과 마찬가지로 윈도우가 겹치면서 데이터가 중복 처리되는 것을 방지하거나 제어하기 위해 옵션을 




# Window Operations
- Spark Streaming은 *windowed computations* 즉 윈도우 기반 연산을 제공한다.
- 소스 DStream 위를 윈도우가 슬라이딩할 때마다 윈도우 내에 있는 소스 RDD가 결합되고 조직되어, 윈도우 DStream의 RDD를 생성한다.


- 윈도우 연산에 필요한 두 가지 매개변수
    1. 윈도우 길이(window length) : 윈도우의 기간
    2. 슬라이딩 간격(sliding interval) : 윈도우 작업이 수행되는 간격 
    - 이 두 매개변수는 소스 DStream의 배치 간격의 배수여야 한다.
- 만약 ㅇ
- ``` java
    // Reduce last 30 seconds of data, every 10 seconds
    JavaPairDStream<String, Integer> windowedWordCounts = pairs.reduceByKeyAndWindow((i1, i2) -> i1 + i2, Durations.seconds(30), Durations.seconds(10));
  ```
