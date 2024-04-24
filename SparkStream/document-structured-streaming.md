# Structured Streaming
- Spark Structured Streaming 3.1.3의 도큐멘테이션을 번역한 글이다.
- 공부 및 정리용으로 작성하고 있기 때문에, 오역 또는 의역이 들어있을 수 있다.
- 출처: https://spark.apache.org/docs/3.1.3/structured-streaming-programming-guide.html

## Programming Model
- Structured Streaming에서 중요한 포인트는 실시간 스트림 데이터를 끊임없이 추가되는 테이블처럼 취급한다는 것이다.
- 이 방식은 배치처리 모델과 매우 흡사한, 새로운 방법의 데이터 처리 방식이다.
- 개발자는 일반적인 배치 처리 쿼리와 유사한 형태로 스트리밍 연산을 표현할 수 있다.
- 즉 스트리밍 처리를 정적 테이블처럼 표현하고, 스파크가 이를 무한한 입력 테이블에 대한 점진적인 쿼리로 실행한다.
    - 여기서 정적 테이블이란 고정된 크기를 가진 데이터를 의미하며, 한 번에 모두 로드하여 처리할 수 있는 테이블을 의미

#### Basic Concepts
- 입력 데이터 스트림을 "입력 테이블"로 취급하고, 스트림에 도착하는 모든 데이터 아이템을 마치 입력 테이블에 추가되는 데이터 row처럼 다룬다.
- 입력된 데이터에 쿼리를 실행하면 결과 테이블이 생성된다.
- 모든 트리거 인터벌(예를들어, 매 1초마다 등)마다 입력 테이블에 새 row가 추가되고, 최종적으로 결과 테이블도 업데이트 된다.
- 결과 테이블이 업데이트 될 때마다 외부 싱크로 변화된 결과 row를 기록하고 싶을 것이다.
- "출력 Output"이란 외부 저장소로 기록되는 것으로 정의된다. 이 때 출력은 몇 가지 모드로 정의될 수 있다.
    - Complete Mode: 모든 결과 테이블이 외부 저장소에 기록된다. 모든 테이블 기록의 핸들링은 저장소 연결부가 담당한다.
    - Append Mode: 지난 트리거 이후에 추가된 새로운 row만 외부 저장소에 기록된다. 현재 결과 테이블의 row들이 변하지 않을 때만 적용 가능하다.
    - Update Mode: 지난 트리거 이후에 결과 테이블에 업데이트 된 row만 외부 스토리지에 기록한다.(Spark 2.1.1부터 사용 가능) Complete mode와 다른점은 오직 변경사항만 아웃풋으로 기록한다는 점이다. 만약 쿼리에 집계가 포함되어 있지 않으면 append mode와 똑같이 동작한다.
- 위 모드들은 특정한 타입의 쿼리들에서 사용 가능하다는 점을 기억하라. 이것에 대해서는 후에 다룰 것이다.

- **Structured Streaming이 전체 테이블을 물리적으로 저장하지는 않는다는 것을 기억하라.**
- Structured Streaming은 스트리밍 데이터 소스에서 사용 가능한 최신 데이터를 읽어들이고, 점진적으로 처리해서 결과를 업데이트하고, 원본 데이터를 폐기한다.
- 오직 결과를 업데이트하는 데 필요한 최소의 중간 상태 데이터만 유지한다.(ex. 이전 예제에서의 중간 계산 결과)
- 이 모델은 다른 스트림 처리 엔진과 매우 큰 차이점을 갖는다.
- 보통의 스트리밍 시스템은 실행중인 집계를 사용자가 직접 유지하도록 하여 결함허용 및 데이터 일관성에 대한 판단을 직접 하도록 한다.
- 하지만 이 모델에서는 새로운 데이터가 있을 때 Spark가 결과 테이블을 업데이트하는 책임을 지므로 사용자가 여기에 관여할 필요가 없다.
- 예를 들어, 이 모델이 이벤트 시간 기반 처리 및 늦게 도착한 데이터를 처리하는 방법을 살펴보자.

#### Handling Event-time and Late Data
- 이벤트 타임은 데이터에 내장된 시간이다.
- 많은 어플리케이션에서 이 이벤트 타임을 기반으로 작업을 수행하고자 한다.
- 예를 들어, IoT 단말기로부터 매 분마다 발생한 이벤트 수를 알고싶다고 한다면, 스파크가 데이터를 수신한 시간이 아니라 데이터가 발생한 시간을 알고싶을 것이다.(이게 바로 데이터의 이벤트 타임이다.)
- 이 모델에서는 이벤트 타임을 매우 자연스럽게 표현한다 - 단말기에서 발생한 각 이벤트는 테이블의 row가 되고, 이벤트 타임은 하나의 컬럼이 된다.
- 이렇게 함으로써 윈도우 기반 집계를 그저 이벤트 타임에서 특별한 유형으로 그룹핑 및 집계처럼 되도록 한다.
- 시간 윈도우는 그룹이고 각 row는 여러 윈도우나 그룹에 속할 수 있다. 따라서 이벤트-타임-윈도우-기반- 집계 쿼리는 정적 데이터 세트(ex. 단말에서 수집된 이벤트 로그)와 데이터 스트림 모두에 일관되게 정의될 수 있어 훨씬 사용자에게 편리하다.

- 게다가 이 모델은 이벤트 타임을 기준으로 예상보다 늦게 도착한 데이터를 자연스럽게 처리한다.
- Spark가 결과 테이블을 업데이트 하고 있기 대문에, 지연된 데이터가 있을 때 이전 집계 업데이트를 완벽하게 제어할 수 있을 뿐 아니라 이전 집계를 정리하여 중간 상태 데이터의 크기를 제한한다.
- Spark 2.1부터 우리는 사용자가 지연 데이터의 임계값을 지정할 수 있도록 하고 그에 따라 엔진이 이전 상태를 정리할 수 있도록 하는 워터마킹을 지원한다.
- 여기에 대해서는 나중에 윈도우 작업 섹션에서 자세히 설명하도록 한다.

#### Fault Tolerance Semantics
- 처음부터 끝까지(end-to-end) 단 한 번만 전송한다는 시멘틱은 Structured Streaming을 디자인 할 때 배경으로 깔린 주요 목표였다.
    - 시멘틱 Semantics: 사전적의미는 '의미론'이지만 프로그래밍에서 말하는 시멘틱이란 프로그램을 실행할 때 따르는 프로세스, 또는 실행되는 방식, 프로토콜 등을 말함
- 이것을 달성하기 위해서 재시작 또는 재처리를 통해 모든 종류의 오류를 처리할 수 있도록 처리의 정확한 진행 상황을 안정적으로 추적할 수 있는 구조적 스트리밍 소스, 싱크 및 실행 엔진을 설계했다.
- 모든 스트리밍 소스에는 스트림의 읽기 위치를 추적하기 위한 오프셋이 있다고 가정한다.
    - Structured Streaming의 오프셋은 Kafka 오프셋 또는 Kinesis 시퀀스 번호와 유사
- 엔진은 체크포인트와 미리쓰기 로그를 사용하여 각 트리거에서 처리되는 데이터의 오프셋 범위를 기록한다.
- 스트리밍 싱크는 재처리될 수 있도록 멱등성을 갖도록 설계되었다.
    - 멱등성: 연산을 여러 번 적용하더라도 결과가 달라지지 않는 성질. 예를 들어 get 메소드로 서버에 어떤 값을 요청해도 반환값은 항상 동일하다.
- 재생 가능한 소스와 멱등성 싱크를 함께 사용하면 어떤 오류가 발생하더라도 '처음부터 끝까지 단 한번만 전송'하는 시멘틱을 보장할 수 있다.

#### API using Datasets and DataFrames
- Spark 2.0부터, 데이터 프레임과 데이터셋은 정적이고 한정된 데이터로 표현되고, streaming은 무한정 데이터로 표현된다.
- 정적인 데이터셋/데이터프레임과 유사한 방식으로, 일반적인 진입점인 SparkSession(Scala/[Java](https://spark.apache.org/docs/3.1.3/api/java/org/apache/spark/sql/SparkSession.html)/Python/R docs)을 사용하여 스트리밍 소스에서 스트리밍 데이터프레임/데이터셋을 생성하고 정적인 것과 동일한 작업을 수행할 수 있다.
- 데이터셋/데이터프레임을 사용하는 데 익숙하지 않다면 [가이드](https://spark.apache.org/docs/3.1.3/sql-programming-guide.html)를 읽도록 강력히 권장한다.


#### Creating streaming DataFrames and streaming Datasets
- 스트리밍 데이터프레임은 *SparkSession.readStream()*이 반환하는 *DataStreamReader* 인터페이스를 통해 생성될 수 있다. (R에서는 SparkSession.readStream() 사용)
- 정적인 데이터프레임을 생성하는 것과 유사하게 소스의 세부 정보(데이터 형식, 스키마, 옵션 등)을 지정할 수 있다.

#### Input Sources
- 몇 가지 빌트인 소스들이 제공된다.
    - File source - 디렉토리에 적힌 파일을 데이터 스트림으로 읽어들인다. 파일은 수정일자 순으로 처리된다. 만약 *latestFirst*가 설정되어 있으면 순서는 반대로 적용된다. 지원되는 파일형식은 CSV, JSON, ORC, Parquet이다. DataStreamReader 도큐멘테이션에 좀더 최신 내용의 목록과 지원파일 형식 등이 나와있다. 주의할 점은 파일은 자동으로 주어진 디렉토리에 원자적으로 배치해야 한다. 대부분의 파일 시스템에서 파일 이동(또는 복사) 작업으로 수행할 수 있다. 즉, 파일을 해당 디렉토리로 옮길 때 파일이 중간에 부분적으로 복사되거나 생성되는 것이 아니라, 파일 전체가 원자적으로(즉, 중간에 어떤 문제도 없이) 해당 디렉토리에 배치되어야 한다는 의미이다.
    - Kafka source - 카프카에서 데이터를 읽어들인다. Kafka broker 버전 0.10.0 또는 그 이상의 버전에서 호환된다. [Kafka Integration Guide](https://spark.apache.org/docs/3.1.3/structured-streaming-kafka-integration.html)에서 세부내용을 확인할 수 있다.
    - Socket source (테스트용) - 소켓 연결에서 UTF8 텍스트 데이터를 읽어들인다. 
    - Rate source (테스트용) - 매 초마다 특정한 숫자의 데이터를 생성해내고, 각 아웃풋 row는 timestamp와 value를 갖고 있다. timestamp는 메시지 발송 시간을 포함하는 Timestamp 타입이고, value는 Long 타입으로 메시지의 개수를 나타내며 첫 번째 row는 0부터 시작한다. 이 소스는 테스트와 벤치마킹을 위해 만들어진 것이다.