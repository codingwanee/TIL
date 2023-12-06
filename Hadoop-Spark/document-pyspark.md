# pyspark
- PySpark 3.5.0 공식 도큐멘트를 번역
- 개인 공부를 위해 정리한 내용이므로, 의역 또는 순서의 변형이 있을 수 있음.
- 출처: https://spark.apache.org/docs/latest/api/python/index.html



## PySpark Overview
- PySpark는 Apache Spark를 위한 파이썬 API이다.
- PySpark는 파이썬을 이용해서 분산처리 환경에서 실시간으로 대용량 데이터를 처리할 수 있도록 한다.
- 또한, PySpark 쉘에서 데이터를 상호적으로 분석하는 기능도 제공한다.
- PySpark는 파이썬의 학습용이성과 Apache Spark의 편의성을 모두 가지고 있어 파이썬에 친숙한 사람이라면 누구든 이용 가능하며, 어떤 사이즈의 데이터라도 처리하고 분석할 수 있다.
- PySpark는 모든 Spark 기능들을 지원한다. 예를 들면 Spark SQL, DataFrames, Structured Steraming, Machine Learning, Spark Core 등등이 있다.


#### Spark SQL and DataFrames
- Spark SQL은 구조화 된 데이터를 처리하기 위한 Apache Spark 모듈이며, SQL 쿼리와 Spark 프로그램을 원활하게 호환되게 하는 역할을 한다.
- PySpark DataFrame은 파이썬과 SQL을 이용하여 데이터를 효과적으로 읽고, 쓰고, 변형하고, 분석할 수 있도록 한다.
- 파이썬 또는 SQL 중에서 어떤 것을 사용하더라도 동일한 내부 엔진을 이용하여 작동하므로 Spark의 동력을 풀파워로 끌어올려 사용할 수 있다.
