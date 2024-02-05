이번에는 DefaultHandlerExceptionResolver를 알아보자!

# DefaultHandlerExceptionResolver

`DefaultHandlerExceptionResolver` 는 스프링 내부에서 발생하는 스프링 예외를 해결한다. <br>
대표적으로 파라미터 바인딩 시점에 타입이 맞지 않으면 내부에서 `TypeMismatchException` 이 발생하는데, 
이 경우 예외가 발생했기 때문에 그냥 두면 서블릿 컨테이너까지 오류가 올라가고, 결과적으로 500 오류가 발생한다. <br>
그런데 파라미터 바인딩은 대부분 클라이언트가 HTTP 요청 정보를 잘못 호출해서 발생하는 문제이다. <br>
HTTP 에서는 이런 경우 HTTP 상태 코드 400을 사용하도록 되어 있다.<br>
`DefaultHandlerExceptionResolver` 는 이것을 500 오류가 아니라 HTTP 상태 코드 400 오류로 변경한다.<br>
스프링 내부 오류를 어떻게 처리할지 수 많은 내용이 정의되어 있다.<br>

[DefaultHandlerExceptionResolver 주요 코드](https://github.com/imkh817/exception-spring/blob/master/src/main/resources/templates/DefaultHandlerExceptionResolver%20주요%20코드.md)<br>

#### 즉, 따로 개발자가 지정한 예외 처리가 아니고, 스프링 내부에서 발생하는 스프링 예외는 `DefaultHandlerExceptionResolver`가 해결해준다고 생각하면 된다.

