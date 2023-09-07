# Spark Streaming
- 실시간 데이터 처리를 위한 Apache Spark의 라이브러리
- SQL 엔진 위에 만들어진 분산 스트림 처리 프로세싱
- kafka, HDFS 등 다양한 소스에서 데이터를 수집해 와서 Spark의 다양한 기능과 함께 사용할 수 있음
- 시간대 별로 데이터를 합쳐(aggregate) 분석할 수 있음
- 체크포인트를 만들어서 부분적인 결함이 발생해도 다시 돌아가서 데이터 처리 가능

## Spark Streaming의 데이터 처리 방식
- 스파크에는 2가지 방식의 스트리밍 처리 API가 있음
    1. DStream
    2. Structured Streaming (구조적 스트리밍)


## DStream 방식의 데이터 처리
#### RDD Resilient Distributed Datasets
    - RDD는 Spark에서 복원 가능한 데이터 블록을 나타내며, Spark의 기본 추상화
    - 분산 환경에서 안정적이고 복원 가능한(resilient) 데이터 구조 제공
    - 각 RDD는 여러 파티션으로 분할되어 클러스터의 다양한 노드에서 병렬처리됨
    - RDD는 지연 연산의 개념을 사용하여 변환 연산을 기록하고, 액션 연산이 호출될 때 실제 연산이 수행됨

#### DStream Discretized Stream
    - Spark Streaming에서 스트림 처리를 위한 고수준의 추상화
    - 연속적인 데이터 스트림을 나타내며, 각 시간 단위마다 RDD의 시퀀스로 구성
    - DStream은 기본적으로 변하지 않는 RDD의 시퀀스라고 생각할 수 있음
    - Spark의 기본 RDD 연산을 사용하여 변환하거나, 사용자 지정함수를 적용하여 스트림 처리를 수행

#### DStream 데이터 처리 방식
- Spark RDD를 기반으로 마이크로-배치 방식 이용
    - 짧은 시간 동안 모인 데이터를 한 번에 처리해서 처리속도를 높이고 실시간으로 데이터 처리
- 다양한 처리 방식 제공
    - RDD(Resilient Distributed Datasets)와 DStream(Distributed Stream)을 사용
    - 데이터를 필터링, 변환, 집계, 머신러닝 모델 적용 가능
- 하나의 클러스터에서 수백 개의 스트림을 처리할 수 있으며 클러스터의 자원 관리를 통해 실시간 처리의 확장성 보장



## 구조적 스트리밍 Structured Streaming
- 스파크의 구조적 API(DataFrame, Dataset, SQL)를 지원하기 때문에 기존 배치성으로 작성된 코드를 대부분 가져다 쓸 수 있음









## Spark Streaming 동작 순서
1. Spark Streaming Context 생성
    - pyspark.streaming 패키지에서 spark streaming을 불러와 spark context 주입하여 사용 가능
2. Socket 설정
    - spark streaming은 특정 포트를 통해 데이터를 받아들임
3. 작업 설정
    - streaming으로 받아온 텍스트에 수행해줄 작업을 설정


#### Spark Streaming 서버


#### Spark 
