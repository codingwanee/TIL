- @SpringBootApplication
    - 해당 클래스를 베이스 패키지로 간주
    - @ComponentScan을 포하하고 있어 굳이 추가해주지 않아도 스프링 빈으로 관리됨
- @Autowired
    - 자동으로 다른 오브젝트를 찾아 연결
- @Component
    - 해당 클래스를 자바 빈으로 등록시키라고 알려주는 어노테이션

- @ComponentScan
    - 베이스 패키지와 그 하위 패키지에서 @Component가 달린 클래스를 찾는다.

- @Bean
    - 해당 오브젝트를 어떻게 생성해야 하는지, 매개변수를 어떻게 넣어줘야 하는지 명시해줄 수 있음
    - 로컬 환경에서 자동으로 연결될 빈 대신 다른 빈을 사용하고 싶거나, 어떤 라이브러리가 스프링 기반이 아니어서 @Component를 추가하지 못하는 경우 등에 이용할 수 있음


- @Builder
    - Builder 클래스를 따로 개발하지 않고도 Builder 패턴을 사용해 오브젝트를 생성할 수 있다.
- @NoArgsConstructor
    - 매개변수가 없는 생성자를 구현
- @AllArgsConstructor
    - 모든 멤버 변수를 매개변수로 받는 생성자를 구현
- @Data
    - Getter/Setter 메소드 구현


- @PathVariable
    - URI의 경로로 넘어오는 값을 변수로 받을 수 있음 ("/{id}")

- @RequestParam
    - 요청 매개변수로 넘어오는 값을 변수로 받을 수 있음 (/id?={id})

- @RequestBody
    - RequestBody로 보내오는 JSON을 지정한 DTO 객체로 변환해 가져오라는 뜻

