# 스칼라의 Future

* scala.concurrent.Future API는 암시적 인자를 활용해서 코드에 필요한 준비 코드를 줄여주는 동시성 도구다.

* 수행할 작업 중 일부를 Future로 감싸면 그 작업을 비동기적으로 수행한다. 그리고 Future API는 결과가 준비된 경우 콜백을 호출해주는 등 결과를 처리할 수 있는 다양한 방법을 제공한다.



#### ExecutionContext

import scala.concurrent.ExecutionContext.Implicits.gloal

* connection pool과 같은 개념
* 퓨쳐가 일반적으로 사용하는 ExecutionContext이며, implicit 키워드로 선언된다. 따라서 