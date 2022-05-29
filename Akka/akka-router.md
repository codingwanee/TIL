# akka-router

* 라우터(router)는 기본적으로 수평 또는 수직 규모 확장을 위한 것이다.
* 수직 확장을 원한다면 같은 작어을 수행하는 인스턴스가 여러 개 필요할 것이다.
* 라우터는 여러 인스턴스 중에 어떤 것이 수신 메시지를 처리할 것인지를 결정한다.


## 몇 가지 일반적인 라우팅 DSL bits 와 bobs
* path : which satisfies the route name part of the route
* get : which tells us that we should go further into the route matching if it’s a GET http request and it matched the path route DSL part
* post: which tells us that we should go further into the route matching if it’s a POST http request and it matched the path route DSL part
* complete : This is the final result from the route


#### get
* path를 사용하고 get 지시문을 사용하여 get route를 설정하며, 다음 complete 를 사용하여 반환 할 HTML을 나타내는 정적 문자열로 경로를 완성

```scala
path("hello") {
  get {
    complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
  }
}

```


#### get item (as json)
* path/get  지시문을 사용하지만 이번에는 Item을 complete 하는데 이는 우리에게 적합한 직렬화 데이터를 생성 할 수있는 JSON 지원으로 인해 수행

```scala
path("randomitem") {
  get {
    // will marshal Item to JSON
    complete(Item("thing", 42))
  }
}
```

#### post
* 들어오는 JSON 문자열을 항목으로 변환하는 작업은 Unmarshaller를 사용하여 수행

```scala
path("saveitem") {
  post {
    // will unmarshal JSON to Item
    entity(as[Item]) { item =>
      println(s"Server saw Item : $item")
      complete(item)
    }
  }
} 

```
