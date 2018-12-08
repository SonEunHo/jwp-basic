#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* 8080으로 포트를 열어둔다.
* 웹어플리케이션 루트를 설정한다.
* 웹어플리케이션 경로밑에 존재하는 서블릿들과 서블릿 필터들을 읽어온다.
* 매핑 경로와 서블릿을 매칭시킨다.
* 디비 메타파일을 읽어와서 디비를 초기화 한다.
* 디스패처 서블릿이 초기화 되면서 매핑 테이블을 정립하고, 각 컨트롤러 객체를 생성해둔다.

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 디스패처 서블릿으로 요청이 들어오고 리퀘스트 매핑테이블을 참조한다.
* localhost:8080으로 요청을 하면 "/"가 requestPath가 된다.
* 요청은 homeController로 넘어간다.
* 요청은 home.jsp로 넘어가서 사용자에게 보여지는데, 이 때 questionDao의 모든 질문들을 긁어온다.
* homecontroller는 home.jsp로 요청을 전달할 때 질문 정보를 함께 넘겨준다.
* jsp에서는 질문 정보를 jsp문법을 사용해서 동적으로 그려준다.

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 
