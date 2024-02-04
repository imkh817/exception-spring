기존에는 에러가 터지면 아래와 같이 WAS까지 에러가 전파되었고 다시 WAS에서 오류페이지를 확인 후 다시 요청을 보냈었다.
```
1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
3. WAS 오류 페이지 확인
4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View
 ```

### 하지만 이러한 과정은 너무 복잡하다. 다른 방법은 없을까 ❓
### `ExceptionResolver` 를 활용하면 예외가 발생했을 때 이런 복잡한 과정 없이 문제를 깔끔하게 해결할 수 있다.
***
## 예시 ✍🏻
**먼저 사용자 정의 예외를 하나 만들자!**<br>
[UserException](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/exception/UserException.java)

**ApiExceptionController - 예외 추가** <br>
[ApiExceptionController](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/api/ApiExceptionController.java)

http://localhost:8080/api/members/user-ex 호출시 `UserException` 이 발생하도록 하였다.<br>
이제 이 예외를 처리하는 `UserHandlerExceptionResolver` 를 만들어보자<br>

**UserHandlerExceptionResolver** <br>
[UserHandlerExceptionResolver](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/resolver/UserHandlerExceptionResolver.java)

**WebConfig에 UserHandlerExceptionResolver 추가** <br>
[WebConfig](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/WebConfig.java)

#### Postman으로 실행 👨🏻‍💻
http://localhost:8080/api/members/ex
```json
{
"ex": "hello.exception.exception.UserException",
"message": "사용자 오류"
}
```
```html
 <!DOCTYPE HTML>
 <html>
 ...
 </html>
```
***
## 정리 ✅

`ExceptionResolver` 를 사용하면 컨트롤러에서 예외가 발생해도 `ExceptionResolver` 에서 예외를 처리해버린다.<br>
따라서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고, 스프링 MVC에서 예외 처리는 끝이 난다. <br>
결과적으로 WAS 입장에서는 정상 처리가 된 것이다.<br> 
이렇게 예외를 이곳에서 모두 처리할 수 있다는 것이 핵심이다.<br>
서블릿 컨테이너까지 예외가 올라가면 복잡하고 지저분하게 추가 프로세스가 실행된다.<br> 
`ExceptionResolver` 를 사용하면 예외처리가 상당히 깔끔해진다.<br>
### 그런데 직접 `ExceptionResolver` 를 구현하려고 하니 상당히 복잡하다🥲<br>


