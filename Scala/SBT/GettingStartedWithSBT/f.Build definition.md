# Build definition
- https://www.scala-sbt.org/1.x/docs/Basic-Def.html 에 대한 번역 및 정리
- 정식 번역이 아니므로 의역 및 오역, 재정리 된 내용이 있음

## 도입부
- 본 내용은 sbt를 설치했다는 가정 하에 작성되었다.
- sbt가 어떻게, 왜 동작하는지에 앞서 예시를 보며 시작해보도록 하자.
- 현 페이지는 `build.sbt` 의 빌드 정의에 대한 내용을 담는다.


## sbt version 명시하기
- 빌드의 버전을 명시하는 것 또한 build definition에 포함된다.
- 버전을 명시함으로써 sbt launcher가 동일한 프로젝트에서 각기 다른 버전으로 견고한 산출물을 낼 수 있도록 한다.
- `project/build.properties`라는 이름의 파일을 생성해서 아래와 같이 sbt version을 써준다
```conf
sbt.version=1.7.1
```

- 만일 여기에 명시된 버전과 로컬 환경의 버전이 맞지 않는 경우, sbt launcher는 알아서 맞는 버전을 다운로드 해 준다.
- 파일이 존재하지 않는다면, sbt launcher는 


