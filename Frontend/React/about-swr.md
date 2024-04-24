# SWR (Stale While Revalidate)

## 각 용어들의 의미
- SSR (Server Side Rendering) : 초기 데이터 fetching에 조첨
- CSR (Client Side Rendering) : 데이터 업데이트에 초점
- SWR (Stale While Revalidate) : CSR 측면에서 데이터 fetching 로직을 단순화하면서 다양한 옵션을 통해 보다 강력하게 만들어주는 라이브러리


## SWR이란
- SWR 공식 한글 홈페이지 설명
    - "SWR"이라는 이름은 HTTP RFC 5861에 의해 알려진 HTTP 캐시 무효 전략인 stale-while-revalidate에서 유래되었습니다. SWR은 먼저 캐시(스태일)로부터 데이터를 반환한 후, fetch 요청(재검증)을 하고, 최종적으로 최신화된 데이터를 가져오는 전략입니다.
    - 출처ㅣ https://swr.vercel.app/ko
- api 주소와 fetch함수를 파라미터로 받아 데이터를 캐싱하고 이를 설정한 옵션, 혹은 mutate 설정에 맞게 재검증하여 클라이언트 데이터를 서버와 동기화 해준다

## 유사 라이브러리와 비교
- react-query
    - SWR과 마찬가지로 위에서 설명한 것과 같이 client side에서 data fetching 시 caching, 서버와의 동기화, pre-fetch 등의 여러 옵션을 활용한 최신 데이터 관리를 위해 나온 라이브러리

- 차이점
    1. global fetcher
    2. garbage collect
    3. request cancellation
