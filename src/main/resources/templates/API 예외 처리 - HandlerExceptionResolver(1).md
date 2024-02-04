### 예외가 발생해서 서블릿을 넘어 WAS까지 예외가 전달되면 HTTP 상태코드가 500으로 처리된다. 
### 발생하는 예외에 따라서 400, 404 등등 다른 상태코드로 처리하고 싶을 떄는 어떻게 해야할까 ❓

📌 예시 
클라이언트가 입력값을 잘못 입력해서 `IllegalArgumentException` 예외가 발생하였다.<br>
이때 BasicController를 사용하게 되면 상태 코드 500 에러가 발생된다.<br>
하지만 클라이언트가 입력 값을 잘못 입력한거기 때문에 상태 코드 400 에러가 적절하다.<br>

**ApiExceptionController 수정** <br>
[ApiExceptionController](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/api/ApiExceptionController.java)

Postman으로 http://localhost:8080/api/members/bad 를 호출하면 상태 코드가 500인 것을 확인할 수 있다.
```json
{
"status": 500,
"error": "Internal Server Error",
"exception": "java.lang.IllegalArgumentException",
"path": "/api/members/bad"
}
```
***
# HandlerExceptionResolver

스프링 MVC는 컨트롤러(핸들러) 밖으로 예외가 던져진 경우 예외를 해결하고, 동작을 새로 정의할 수 있는 방법을 제공한다.<br>
컨트롤러 밖으로 던져진 예외를 해결하고, 동작 방식을 변경하고 싶으면 `HandlerExceptionResolver` 를 사용하면 된다. <br>
줄여서 `ExceptionResolver` 라 한다.<br>

### ExceptionReslover 적용 전 ❗️
![ExceptionReslover 적용 전](https://blog.kakaocdn.net/dn/dgaoRI/btsEnTVjIyO/FhwFKajqypkc5hDeHASu8k/img.png)

### ExceptionReslover 적용 후 ❗️
![ExceptionReslover 적용 후](https://blog.kakaocdn.net/dn/clwl8g/btsEna3TZdT/5nPKdmFKOO2WnAHBgBCHa0/img.png)

**ExceptionResolver 적용 후에는 Controller에서 예외 발생 시 Dispatcher Servlet에서 WAS로 예외를 던지는게 아니라
ExceptionResolver로 예외를 던져 ExceptionResolver에서 예외를 처리하게 된다.**

**ExceptionResolver 적용 전 순서 📌** <br>
Controller(예외발생) -> 핸들러 어댑터 -> Dispatcher Servlet -> WAS <br><br>
**ExceptionResolver 적용 후 순서 📌**<br>
Controller(예외발생) -> 핸들러 어댑터 -> Dispatcher Servlet -> ExceptionResolver(예외 처리)<br>
***
**HandlerExceptionResolver - 인터페이스**
```java
public interface HandlerExceptionResolver {
    @Nullable
    ModelAndView resolveException(HttpServletRequest var1, HttpServletResponse var2, @Nullable Object var3, Exception var4);
    /**
     * resolveException: 예외를 해결하고 해당 예외에 대한 ModelAndView를 반환하는 메서드

     HttpServletRequest var1: 현재 요청과 관련된 HTTP 요청 객체

     HttpServletResponse var2: 현재 요청과 관련된 HTTP 응답 객체

     @Nullable Object var3: 예외가 발생한 핸들러(컨트롤러), 핸들러를 구분할 수 없는 경우 null이 될 수 있다.

     Exception var4: 발생한 예외

     @Nullable ModelAndView: 예외를 해결한 후에 반환할 ModelAndView 객체,null 을 반환하면, 다음 ExceptionResolver 를 찾아서 실행한다.
                             만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고, 기존에 발생한 예외를 서블릿 밖으로 던진다.


     */
}
```
**MyHandlerExceptionResolver** <br>
[MyHandlerExceptionResolver]()

`ExceptionResolver` 가 `ModelAndView` 를 반환하는 이유는 마치 try, catch를 하듯이, `Exception` 을 처리해서 정상 흐름 처럼 변경하는 것이 목적이다. <br>
이름 그대로 `Exception` 을 Resolver(해결)하는 것이 목적이다.<br>
여기서는 `IllegalArgumentException` 이 발생하면 `response.sendError(400)` 를 호출해서 HTTP 상태 코드를 400으로 지정하고, 빈 `ModelAndView` 를 반환한다.<br>

**ExceptionResolver 활용** 
- 예외 상태 코드 변환
    - 예외를 `response.sendError(xxx)` 호출로 변경해서 서블릿에서 상태 코드에 따른 오류를 처리하도록 위임
    - 이후 WAS는 서블릿 오류 페이지를 찾아서 내부 호출, 예를 들어서 스프링 부트가 기본으로 설정한 `/ error` 가 호출됨
- 뷰 템플릿 처리
    - `ModelAndView` 에 값을 채워서 예외에 따른 새로운 오류 화면 뷰 렌더링 해서 고객에게 제공
- API 응답 처리
    - `response.getWriter().println("hello");` 처럼 HTTP 응답 바디에 직접 데이터를 넣어주는 것도 가능하다.
    - 여기에 JSON 으로 응답하면 API 응답 처리를 할 수 있다.

**WebConfig - 수정**<br>
[WebConfig - 수정](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/WebConfig.java)

#### Postman으로 실행 👨🏻‍💻
http://localhost:8080/api/members/ex
```json
{
    "timestamp": "2024-02-04T06:33:36.400+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/members/ex"
}
```
http://localhost:8080/api/members/bad
```json
{
    "timestamp": "2024-02-04T06:31:42.752+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/members/bad"
}
```