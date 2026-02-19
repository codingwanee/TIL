# Same-Origin Policy (SOP)

## 1. Same-Origin Policy란?

**Same-Origin Policy(동일 출처 정책)** 는\
웹 브라우저가 보안을 위해 서로 다른 출처(origin) 간의 리소스 접근을
제한하는 정책입니다.

> 다른 사이트의 데이터는 함부로 읽지 못하게 하는 브라우저의 기본 보안
> 규칙입니다.

---

## 2. Origin(출처)이란?

Origin은 아래 3가지 요소가 모두 같아야 동일합니다:

- Protocol (http / https)
- Host (domain)
- Port

### 예시

URL 동일 출처 여부

---

https://example.com 기준
https://example.com:443 동일
http://example.com 다름 (프로토콜)
https://api.example.com 다름 (도메인)
https://example.com:3000 다름 (포트)

---

## 3. 왜 필요한가?

SOP가 없다면:

1.  사용자가 bank.com에 로그인
2.  악성 사이트 evil.com 방문
3.  evil.com이 bank.com의 데이터를 읽음

→ 세션 탈취 및 개인정보 유출 가능

그래서 브라우저는 다음을 제한합니다:

- 다른 출처의 DOM 접근 금지
- 다른 출처의 AJAX 응답 읽기 금지
- 다른 출처의 쿠키 접근 금지

---

## 4. 차단되는 예시

```javascript
fetch("https://other-site.com/data");
```

→ CORS 허용이 없으면 응답을 읽을 수 없음

```javascript
iframe.contentDocument;
```

→ 다른 출처라면 접근 불가

---

## 5. 허용되는 경우

브라우저는 "읽기"를 막는 것이지, 요청 자체는 막지 않습니다.

```html
<img src="https://other-site.com/image.png" />
<script src="https://cdn.com/script.js"></script>
<link href="https://cdn.com/style.css" />
```

→ 리소스 로딩은 가능

---

## 6. CORS와의 관계

CORS (Cross-Origin Resource Sharing)는\
서버가 특정 출처에 대해 읽기 접근을 허용하는 메커니즘입니다.

예:

    Access-Control-Allow-Origin: https://example.com

이 헤더가 있으면 브라우저가 응답 읽기를 허용합니다.

---

## 7. 강제 사항인가?

✔️ 결론: **브라우저가 강제로 적용하는 보안 정책**입니다.

- W3C 표준에 정의됨
- 모든 주요 브라우저가 기본 적용
- 일반 사용자 환경에서는 비활성화 불가

### 예외 상황

- 서버-서버 통신 (Node.js, curl, Postman 등) → SOP 적용 안 됨
- 개발용 브라우저 실행 옵션 → `--disable-web-security` (실서비스에서는
  사용 불가)

---

## 한 줄 요약

> Same-Origin Policy는 브라우저가 강제로 적용하는 기본 보안 정책이며,
> 다른 출처의 데이터를 읽지 못하도록 막는 규칙이다.
