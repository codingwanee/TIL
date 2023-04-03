# React 공부하기


## 리액트 동작 원리
1. 리액트 컴포넌트는 React.createElement를 통해 엘레먼트에 대한 정보를 가지는 Object를 생성
2. 이를 In-Memory에 저장
3. ReactDOM.render 함수를 통해 Web API(document.createElement)를 이용해서 실제 웹 브라우저에 그려줌


## JSX
- JSX란, JavaScript를 확장한 문법으로, 일종의 표현식이다.
- JSX는 템플릿 언어처럼 보일 수 있지만, 자바스크립트를 기반으로 하고 있다.
- 컴파일이 끝나면 JSX 표현식이 정규 js 함수 호출이 되고, js 객체로 인식된다.
- 즉, JSX를 if 구문 및 for loop 안에 사용하고, 변수에 할당하고, 인자로 받아들이고, 함수로부터 반환할 수 있다.
- JSX는 HTML보다는 자바스크립트에 가깝기 때문에, React DOM은 HTML 어트리뷰트 이름 대신 camelCase 프로퍼티 명명 규칙을 사용
- 리액트에서 React.createElement()는 HTML 변환을 위한 코드이다. 바벨+JSX를 이용하면 이 함수를 호출하지 않고도 HTML 태그를 변수에 넣거나 리턴할 수 있다.
    - 바벨: ES6 -> 5로 변환해주는 컴파일러
- 쉽게 말해, JSX는 HTML 태그를 변수로 할당하고, 호출, 리턴할 수 있는 확장 문법이다.


#### JSX 속성 정의
- 따옴표: 문자열 값에 사용
- 중괄호: 어트리뷰트에 표현식 삽입


## DOM(Document Object Model)
- 웹페이지에 대한 인터페이스.
- HTML 문서를 객체 기반으로 표현한 것
- 객체로 표현하여 HTML의 구조와 내용을 객체 모델로 바꿔 다양한 프로그램에서 쉽게 사용할 수 있다.


## React 엘리먼트
- React 엘리먼트는 DOM 엘리먼트와 달리 일반 객체이며(plain object) 쉽게 생성할 수 있다.
- '컴포넌트'와 개념을 혼동하기 쉬우나, 엘리먼트는 컴포넌트의 '구성요소'이다.


#### DOM에 엘리먼트 렌더링하기
- DOM에 엘리먼트 렌더링
    - React로 구현된 애플리케이션은 일반적으로 하나의 루트 DOM 노드가 있다.
    - React를 기존 앱에 통합하려는 경우 원하는 만큼 많은 수의 독립된 루트 DOM 노드가 있을 수 있다.
    - React 엘리먼트를 렌더링 하기 위해서는 우선 DOM 엘리먼트를 ReactDOM.createRoot()에 전달한 다음, React 엘리먼트를 root.render()에 전달해야 한다.
    ```javascript
    const root = ReactDOM.createRoot(
        document.getElementById('root')
    );
    const element = <h1>Hello, world</h1>;
    root.render(element);
    ```
- 렌더링 된 엘리먼트 업데이트하기
    - React 엘리먼트는 불변객체이다.
    - 엘리먼트를 생성한 이후에는 해당 엘리먼트의 자식이나 속성을 변경할 수 없다.


## 컴포넌트 Component
- 정의
    - 페이지에 렌더링할 React 엘리먼트를 반환하는 재사용 가능한 작은 코드 조각.
    - 엘리먼트의 조각들을 모아 모듈화한 것
- 종류
    - functional component
    - class component
- 특징
    - props와 state를 가지는 object
    - 컴포넌트의 이름은 항상 대문자로 시작해야 한다.
    - 컴포넌트는 기능별로 나눌 수 있으며, 다른 컴포넌트 안에서 사용할 수 있다.
    - "props"라고 하는 임의의 입력을 받은 후, 화면에 어떻게 표시되는지를 기술하는 React 엘리먼트를 반환한다.


## 엘리먼트 Element
- 정의
    - React 애플리케이션을 구성하는 블록
- 특징
    - 화면에 보이는 것들을 기술한다.
    - React 엘리먼트는 변경되지 않는다.
    - 일반적으로 엘리먼트는 직접 사용되지 않고 컴포넌트로부터 반환된다.


## 속성 props
- 정의
    - props는 컴포넌트의 임력값으로, 부모 컴포넌트로부터 자식 컴포넌트로 전달된 데이터이다.

- 특징
    - props는 특별한 경우를 제외하고는 HTML 어트리뷰트와 유사하게 작동한다.
    - props는 읽기 전용! 어떤 방식으로든 수정해서는 안 된다.
        - 만약 사용자의 입력 또는 네트워크 응답에 반응하여 어떤 값을 수정해야 한다면 **state**를 사용할 수 있다.
    - props.childeren
        - 모든 컴포넌트에서 props.children을 사용할 수 있다.
        - props.children은 컴포넌트의 여는 태그와 닫는 태그 사이의 내용을 포함한다.
        ```javascript
        // Hello world! 문자열이 Sample 컴포넌트의 props.children
        <Sample>Hello World!</Sample>
        ```


## 상태 state
- 컴포넌트와 관련된 일부 데이터가 시간에 따라 변경될 경우 해당 상태
- state와 props의 차이점
    - props는 부모 컴포넌트로부터 전달받음 / 컴포넌트가 변경 불가
    - state는 컴포넌트에서 관리됨 / 변경 가능


## 클래스 class
- 클래스를 정의하는 선언
- 프로토타입 기반 상속을 사용하여, 주어진 이름의 새로운 클래스를 만듬


## 상수 const
- 블록 범위의 상수를 선언(block-scop[e])
- 값을 재할당할 수 없으며 다시 선언할 수 없음
- 구문
    - nameN: 상수의 이름. 아무 유요한 식별자
    - valueN: 상수의 값. 아무 유효한 표현식


## root
- React Element를 root라는 id를 가지는 div DOM Element 아래에 SubTree 형태로 넣어준다.
- 리액트의 업데이트는 `enqueueUpdate` 내부에서 공유된 전체 업데이트 이벤트 큐가 있고, 해당 작업들이 큐에서 순차적으로 이루어지며 Root에 반영되는 구조


## 싱글 페이지 애플리케이션 Single Page Application
- 하나의 HTML 페이지와 애플이케이션 실행에 필요한 Javascript와 CSS같은 모든 자산을 로드하는 애플리케이션
- 페이지 또는 후속 페이지의 상호작용은 서버로부터 새로운 페이지를 불러오지 않으므로 페이지가 다시 로드되지 않는다.


## CDN Content Delivery Network
- 전 세계의 서버 네트워크에서 캐시된 정적 콘텐츠를 제공


## Ref
- 컴포넌트에 접근할 수 있는 특수한 어트리뷰트
- React.createRef()함수, 콜백함수, 혹은 문자열로 생성할 수 있다.
- ref 어트리뷰트가 콜백함수일 경우 함수는 DOM 엘리먼트나 class 인스턴스를 인자로 받는다.(직접 접근)
- 최대한 적게 사용하는 것을 권장
