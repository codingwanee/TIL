
# 언어 또는 프레임워크별 구동 방식

## 자바 웹 애플리케이션 Java Web Application
#### 순서
1. 서버 실행
2. 가장 처음으로 프로젝트의 META-INF/context.xml 읽어옴
    - 주로 db에 대한 정보가 들어있음 (JDNI 등)
3. web.xml
4. root-context.xml
    - 웹과 관련 없는 설정
    - DispatcherServlet이 생성되기 전에 읽히는 파일이므로, 컨트롤러를 이곳에서 설정하면 안 됨
    - model : component-scal or bean
    - aop : component-scan
    - db : DataSource 등
5. servlet-context.xml
    - 웹에 관련된 설정
    - annotation-driven
    - resource : 요청에 대해 static resource 제공
        - mapping="요청주소", location="실제파일주소" 형식


## 스프링 프레임워크 Spring Framework
#### 순서
1. WAS(보통 Tomcat)에 의해 web.xml 로딩
2. web.xml에 등록되어 있는 ContextLoaderListener 생성
    - ContextLoaderListener
        - ServletContextListener 인터페이스를 구현하고 있으며, ApplicationContext를 생성
        - 서블릿을  초기화하는 용도로 사용
        - contextConfigLocation 파라미터로 load 할 수 있는 설정파일을 지정 가능
    - ApplicationContext : IoC 엔진이며, 빈의 생명주기 담당
3. ContextLoaderListener가 root-context.xml 로딩
4. root-context.xml에 등록되어 있는 설정에 따라 Spring Container(ROOT) 구동
5. 클라이언트 -- request --> Web Application
6. DispatcherServlet 생성
7. DispatcherServlet이 servlet-context.xml 로딩
    - DispatcherServlet은 FrontController의 역할을 수행
8. 두번째 Spring Container 구동되며, 응답에 맞는 PageController 들이 동작됨
