지금까지 HTTP 상태 코드를 변경하고, 스프링 내부 예외의 상태코드를 변경하는 기능도 알아보았다. <br>
그런데 `HandlerExceptionResolver` 를 직접 사용하기는 복잡하다. <br>
API 오류 응답의 경우 `response` 에 직접 데이터를 넣어야해서 매우 불편하고 번거롭다. <br>
`ModelAndView` 를 반환해야 하는 것도 API에는 잘 맞지 않는다.<br>
스프링은 이 문제를 해결하기 위해 `@ExceptionHandler` 라는 매우 혁신적인 예외 처리 기능을 제공한다. <br>
***
# @ExceptionHandler

### HTML 화면 오류 vs API 오류
웹 브라우저에 HTML 화면을 제공할 때는 오류가 발생하면 `BasicErrorController` 를 사용하는게 편하다. <br>
이때는 단순히 5xx, 4xx 관련된 오류 화면을 보여주면 된다. `BasicErrorController` 는 이런 메커니즘이 모두 구현해되어있다.<br>
그런데 API는 각 시스템 마다 응답의 모양도 다르고, 스펙도 모두 다르다. <br>
예외 상황에 단순히 오류 화면을 보여주는 것 이 아니라, 예외에 따라서 각각 다른 데이터를 출력해야 할 수도 있다.<br> 
그리고 같은 예외라고 해도 어떤 컨트롤러에서 발 생했는가에 따라서 다른 예외 응답을 내려주어야 할 수 있다.<br>
앞서 이야기했듯이, 예를 들어 상품 API와 주문 API는 오류가 발생했을 때 응답의 모양이 완전히 다를 수 있다.<br>
결국 지금까지 살펴본 `BasicErrorController` 를 사용하거나 `HandlerExceptionResolver` 를 직접 구현하는 방식으로 API 예외를 다루기는 쉽지 않다.<br>


### API 예외처리를 할 떄 어려웠던 점
- `HandlerExceptionResolver` 를 떠올려 보면 `ModelAndView` 를 반환해야 했다. 이것은 API 응답에는 필요하지 않다.
- API 응답을 위해서 `HttpServletResponse` 에 직접 응답 데이터를 넣어주었다.
  - 스프링 컨트롤러에 비유하면 마치 과거 서블릿을 사용하던 시절로 돌아간 것 같다.
- 특정 컨트롤러에서만 발생하는 예외를 별도로 처리하기 어렵다. 
  - 예를 들어서 회원을 처리하는 컨트롤러에서 발생하는 `RuntimeException` 예외와 상품을 관리하는 컨트롤러에서 발생하는 동일한 `RuntimeException` 예외를 서로 다른 방식으로 처리하고 싶다면 어떻게 해야할까?

### @ExceptionHandler 
스프링은 API 예외 처리 문제를 해결하기 위해 `@ExceptionHandler` 라는 애노테이션을 사용하는 매우 편리한 예외 처리 기능을 제공하는데, 이것이 바로 `ExceptionHandlerExceptionResolver` 이다. <br>
스프링은 `ExceptionHandlerExceptionResolver` 를 기본으로 제공하고, 기본으로 제공하는 `ExceptionResolver` 중에 [우선순위](https://github.com/imkh817/exception-spring/blob/master/src/main/resources/templates/스프링이%20제공하는%20ExceptionResolver%20우선%20순위.md)도 가장 높다.
***

## 예제 ✍🏻
예외가 발생했을 때 API 응답으로 사용하는 객체를 만들어보자!<br>
[ErrorResult](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/exhandler/ErrorResult.java)

예외를 발생시킬 Controller도 하나 만들자!<br>
[ApiExceptionControllerV2](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/api/ApiExceptionControllerV2.java)

### @ExceptionHandler 예외 처리 방법
`@ExceptionHandler` 애노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 된다. <br>
해당 컨트롤러에서 예외가 발생하면 이 메서드가 호출된다. 참고로 지정한 예외 또는 그 예외의 자식 클래스는 모두 잡을 수 있다.<br>


### 다양한 예외
다음과 같이 다양한 예외를 한번에 처리할 수 있다.<br>
```java
 @ExceptionHandler({AException.class, BException.class})
 public String ex(Exception e) {
     log.info("exception e", e);
 }
```
### 예외 생략
`@ExceptionHandler` 에 예외를 생략할 수 있다. 생략하면 메서드 파라미터의 예외가 지정된다.<br>
```java
 @ExceptionHandler
 public ResponseEntity<ErrorResult> userExHandle(UserException e) {}
```

### Postman 실행 👨🏻‍💻
**Postman 실행** <br>
http://localhost:8080/api2/members/bad <br>
**IllegalArgumentException 처리** 
```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandle(IllegalArgumentException e) {
log.error("[exceptionHandle] ex", e);
return new ErrorResult("BAD", e.getMessage());
}
```
**실행 흐름**
컨트롤러를 호출한 결과 `IllegalArgumentException` 예외가 컨트롤러 밖으로 던져진다. <br>
예외가 발생했으로 `ExceptionResolver` 가 작동한다. 가장 우선순위가 높은`ExceptionHandlerExceptionResolver` 가 실행된다.<br>
`ExceptionHandlerExceptionResolver` 는 해당 컨트롤러에 `IllegalArgumentException` 을 처리 할 수 있는 `@ExceptionHandler` 가 있는지 확인한다.<br>
`illegalExHandle()` 를 실행한다. `@RestController` 이므로 `illegalExHandle()` 에도 `@ResponseBody` 가 적용된다.<br>
따라서 HTTP 컨버터가 사용되고, 응답이 다음과 같은 JSON으로 반환된다. `@ResponseStatus(HttpStatus.BAD_REQUEST)` 를 지정했으므로 HTTP 상태 코드 400으로 응답한다.<br><br>
**결과** 
```json
{
  "code": "BAD",
  "message": "잘못된 입력 값"
}
```

### 기타
**HTML 오류 화면** <br>
다음과 같이 `ModelAndView` 를 사용해서 오류 화면(HTML)을 응답하는데 사용할 수도 있다. <br>
```java
 @ExceptionHandler(ViewException.class)
 public ModelAndView ex(ViewException e) {
     log.info("exception e", e);
     return new ModelAndView("error");
}
```
***
# @ControllerAdvice

#### `@ExceptionHandler` 를 사용해서 예외를 깔끔하게 처리할 수 있게 되었지만, 정상 코드와 예외 처리 코드가 하나의 컨트롤러에 섞여 있다. 
#### `@ControllerAdvice` 또는 `@RestControllerAdvice` 를 사용하면 둘을 분리할 수 있다.

#### 🙏 ApiExceptionControllerV2를 분리해야되지만, 나중에 내가 또 공부할떄 편하게 볼 수 있게 끔 ApiExceptionControllerV3를 만들겠다. ️<br>
**ApiExceptionControllerV3 - 추가** <br>
[ApiExceptionControllerV3](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/api/ApiExceptionControllerV3.java) <br>

**ExControllerAdvice - 추가** <br>
[ExControllerAdvice](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/exhandler/advice/ExControllerAdvice.java) <br>

**실행** <br>
- http://localhost:8080/api3/members/bad
```json
{
    "code": "BAD REQUEST",
    "message": "잘못된 입력 값"
}
```
- http://localhost:8080/api3/members/user-ex
```json
{
    "code": "User-Ex",
    "message": "사용자 오류"
}
```
- http://localhost:8080/api3/members/ex
```json
{
    "code": "EX",
    "message": "내부 오류"
}
```
### 정리 ✍🏻
**@ControllerAdvice** <br>
- `@ControllerAdvice` 는 대상으로 지정한 여러 컨트롤러에 `@ExceptionHandler` , `@InitBinder` 기능을 부여해주는 역할을 한다. <br>
- `@ControllerAdvice` 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다. (글로벌 적용) 
- `@RestControllerAdvice` 는 `@ControllerAdvice` 와 같고, `@ResponseBody` 가 추가되어 있다. `@Controller` , `@RestController` 의 차이와 같다.

**대상 컨트롤러 지정 방법 (스프링 공식 문서)** <br>
```java
// Target all Controllers annotated with @RestController
 @ControllerAdvice(annotations = RestController.class)
 public class ExampleAdvice1 {}
 // Target all Controllers within specific packages
 @ControllerAdvice("org.example.controllers")
 public class ExampleAdvice2 {}
 // Target all Controllers assignable to specific classes
 @ControllerAdvice(assignableTypes = {ControllerInterface.class,
 AbstractController.class})
public class ExampleAdvice3 {}
```
#### 스프링 공식 문서 예제에서 보는 것 처럼 특정 애노테이션이 있는 컨트롤러를 지정할 수 있고, 특정 패키지를 직접 지정 할 수도 있다. 
#### 패키지 지정의 경우 해당 패키지와 그 하위에 있는 컨트롤러가 대상이 된다. 그리고 특정 클래스를 지정할 수도 있다.
#### 대상 컨트롤러 지정을 생략하면 모든 컨트롤러에 적용된다.