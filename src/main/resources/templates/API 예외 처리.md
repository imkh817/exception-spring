API 예외 처리는 어떻게 해야할까?<br>
HTML 페이지의 경우 지금까지 설명했던 것 처럼 4xx, 5xx와 같은 오류 페이지만 있으면 대부분의 문제를 해결할 수 있다.<br>
그런데 API의 경우에는 생각할 내용이 더 많다. 오류 페이지는 단순히 고객에게 오류 화면을 보여주고 끝이지만, API는 각 오류 상황에 맞는 오류 응답 스펙을 정하고, JSON으로 데이터를 내려주어야 한다.<br>
지금부터 API의 경우 어떻게 예외 처리를 하면 좋은지 알아보자.<br>
API도 오류 페이지에서 설명했던 것 처럼 처음으로 돌아가서 서블릿 오류 페이지 방식을 사용해보자.<br>

**WebServerCustomizer 다시 동작**
[WebServerCustomizer](https://github.com/imkh817/exception-spring/blob/master/src/main/java/home/exception/WebServerCustomizer.java)

**API Exception Controller**
[API Exception Controller]()

단순히 회원을 조회하는 기능을 하나 만들었고, 예외 테스트를 위해 URL에 전달된 `id` 의 값이 `ex` 이면 예외가 발생하도록 코드를 작성하였다.<br>
### Postman으로 테스트 👨🏻‍💻
**정상 호출** `http://localhost:8080/api/members/spring`
```json
 {
     "memberId": "spring",
     "name": "hello spring"
}
```
**예외 발생 호출** `http://localhost:8080/api/members/ex`
```html
 <!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
    <div class="py-5 text-center">
        <h2>500 오류 화면</h2> </div>
    <div>
        <p>오류 화면 입니다.</p>
    </div>
    <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```
**API를 요청했는데, 정상의 경우 API로 JSON 형식으로 데이터가 정상 반환된다. <br>
그런데 오류가 발생하면 우리가 미리 만들어둔 오류 페이지 HTML이 반환된다. <br>
클라이언트는 정상 요청이든, 오류 요청이든 JSON이 반환되기를 원한다. <br>
웹 브라우저가 아닌 이상 HTML을 직접 받아서 할 수 있는 것은 별로 없기 때문이다. <br>
문제를 해결하려면 오류 페이지 컨트롤러도 JSON 응답을 할 수 있도록 수정해야 한다. <br>**

[ErrorPageController - API 응답 추가]()

`produces = MediaType.APPLICATION_JSON_VALUE` 의 뜻은 클라이언트가 요청하는 HTTP Header의
`Accept` 의 값이 `application/json` 일때 해당 메서드가 호출된다는 것이다. <br>
결국 클라어인트가 받고 싶은 미디어 타입이 json이면 이 컨트롤러의 메서드가 호출된다.<br>
응답 데이터를 위해서 `Map` 을 만들고 `status` , `message` 키에 값을 할당했다. Jackson 라이브러리는 `Map` 을 JSON 구조로 변환할 수 있다.<br>
`ResponseEntity` 를 사용해서 응답하기 때문에 메시지 컨버터가 동작하면서 클라이언트에 JSON이 반환된다. <br>
포스트맨을 통해서 다시 테스트 해보자. `http://localhost:8080/api/members/ex`<br>
```json
{
"message": "잘못된 사용자", "status": 500
}
```
HTTP Header에  `Accept` 가 `application/json` 이면, json 형태로 출력되고,<br>
HTTP Header에 `Accept` 가 `application/json` 이 아니면, 기존 오류 응답인 HTML 응답이 출력되는 것을 확인 할 수 있다.



