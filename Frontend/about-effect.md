# 리액트 함수의 effect

## side effect

- 어떤 함수가 동작을 할 때, input-output 이외의 다른 외부 값을 조작하면 이 함수에 side effect가 있다고 표현
- 컴퓨터 과학에서 함수가 결과값 이외에 다른 상태를 변경시킬 때 부작용이 있다고 말함

```javascript
import { useEffect } from 'react';

function hello({name}) {                   // Input
  useEffect(() => {
    document.title = `${name}님 안녕하세요!`; // Side Effect
  }, [name]);
  
  return <div>{`${name}님 안녕하세요!`}</div>; // Output
}
```

## use effect
- 위에서 말한 side effect들은 함수 안에서 그냥 실행시키면 안 된다.
- 함수 컴포넌트 리턴 값은 UI 요소이고 state, props의 변화가 있을 때마다 함수가 실행되는데, 즉 렌더링 될 때마다 함수 안의 로직이 실행된다는 뜻이다.

- 리액트는 side effect를 위해 `useEffect` 훅을 제공
- `useEffect`는 side effect를 렌더링 이후에 발생시킴
- `useEffect`가 수행되는 시점에 이미 DOM이 업데이트됨을 보장한다는 뜻
- 따라서 `useEffect`를 이용하면 side effect가 렌더링에 영향을 주지 않음

```javascript
import { useEffect } from "react"

useEffect(실행시킬 동작, [타이밍])

useEffect(() => {
  //SideEffect
})              // 매 렌더링마다 SideEffect가 실행되어야 하는 경우

useEffect(() => {
  //SideEffect
}, [value])     // SideEffect가 첫번째 렌더링 이후 한번 실행되고, 이후 특정 값의 업데이트를 감지했을 때마다 실행되어야하는 경우

useEffect(() => {
  //SideEffect
}, [])          // SideEffect가 첫번째 렌더링 이후 한번 실행되고, 이후 어떤 값의 업데이트도 감지하지 않도록 해야 하는 경우

```
