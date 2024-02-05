스프링 부트가 기본으로 제공하는 `ExceptionResolver` 는 다음과 같다. <br>
`HandlerExceptionResolverComposite` 에 다음 순서로 등록 <br>
1. `ExceptionHandlerExceptionResolver`
2. `ResponseStatusExceptionResolver`
3. `DefaultHandlerExceptionResolver` -> 우선순위가가장낮다. <br>

**ExceptionHandlerExceptionResolver** <br>
`@ExceptionHandler` 을 처리한다. API 예외 처리는 대부분 이 기능으로 해결한다.<br><br>
**ResponseStatusExceptionResolver** <br> 
HTTP 상태 코드를 지정해준다.<br>
예시) `@ResponseStatus(value = HttpStatus.NOT_FOUND)` <br> <br>
**DefaultHandlerExceptionResolver** <br>
스프링 내부 기본 예외를 처리한다. <br>

### 가장 쉬운 2번 ResponseStatusExceptionReslover를 먼저 알아보자!
***
# ResponseStatusExceptionResolver

`ResponseStatusExceptionResolver` 는 예외에 따라서 HTTP 상태 코드를 지정해주는 역할을 한다.<br><br>
다음 두 가지 경우를 처리한다. 
- `@ResponseStatus` 가 달려있는 예외 
- `ResponseStatusException` 예외
***
### `@ResponseStatus` 가 달려있는 예외 예시
클라이언트가 잘못 입력했을때 발생할 `BadRequestException` 예외를 하나 만들어보자!<br>
[BadRequestException](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/exception/BadRequestException.java)

`BadRequestException` 예외가 컨트롤러 밖으로 넘어가면 `ResponseStatusExceptionResolver` 예외가 해당 애노테이션을 확인해서
오류 코드를 `HttpStatus.BAD_REQUEST` (400)으로 변경하고, 메시지도 담는다.<br>
`ResponseStatusExceptionResolver` 코드를 확인해보면 결국 `response.sendError(statusCode, resolvedReason)` 를 호출하는 것을 확인할 수 있다.
`sendError(400)` 를 호출했기 때문에 WAS에서 다시 오류 페이지( `/error` )를 내부 요청한다.<br>
[ResponseStatusExceptionResolver 주요 코드](https://github.com/imkh817/exception-spring/blob/master/src/main/resources/templates/ResponseStatusExceptionResolver%20주요%20코드.md)

**ApiExceptionController - 추가** <br>
[ApiExceptionController](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/api/ApiExceptionController.java)

Postman 실행<br>
http://localhost:8080/api/response-status-ex1
```json
{
    "timestamp": "2024-02-05T04:03:45.059+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/response-status-ex1"
}
```

**메시지 기능** <br>
`reason` 을 `MessageSource` 에서 찾는 기능도 제공한다. `reason = "error.bad"`<br>
**BadRequestException - 수정**<br>
[BadRequestException](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/exception/BadRequestException.java)<br>
**messages.properties - 추가**<br>
[messages.properties](https://github.com/imkh817/exception-spring/blob/master/src/main/resources/messages.properties)<br>
**application.properties - 추가**<br>
[messages.properties](https://github.com/imkh817/exception-spring/blob/master/src/main/resources/application.properties)<br>

Postman 실행
http://localhost:8080/api/response-status-ex1
```json
{
  "timestamp": "2024-02-05T04:12:01.870+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "(메시지 사용) 사용자 입력 오류",
  "path": "/api/response-status-ex1"
}
```
***
### `ResponseStatusException` 예외
`@ResponseStatus` 는 개발자가 직접 변경할 수 없는 예외에는 적용할 수 없다. <br>
**✍🏻 예시 : 내가 코드를 수정할 수 없는 라이브러리의 예외 코드 -> 애노테이션을 직접 넣어야되는데 라이브러리의 코드에는 넣을 수 없다🥲**<br>
추가로 애노테이션을 사용하기 때문에 조건에 따라 동적으로 변경하는 것도 어렵다.<br>
이때는`ResponseStatusException` 예외를 사용하면 된다.<br>

**ApiExceptionController - 추가** <br>
[ApiExceptionController](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/api/ApiExceptionController.java)<br>
Postman 실행
http://localhost:8080/api/response-status-ex2
```json
{
  "timestamp": "2024-02-05T04:25:22.884+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "(메시지 사용) 사용자 입력 오류",
  "path": "/api/response-status-ex2"
}
```


