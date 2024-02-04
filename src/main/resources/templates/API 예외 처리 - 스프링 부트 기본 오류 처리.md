API 예외 처리도 스프링 부트가 기본적으로 제공하는 오류 방식으로 처리할 수 있다.

### 스프링 부트가 제공하는 BasicErrorController 코드
```java
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class BasicErrorController extends AbstractErrorController {
    @RequestMapping(
            produces = {"text/html"}
    )
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = this.getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
        response.setStatus(status.value());
        ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
        return modelAndView != null ? modelAndView : new ModelAndView("error", model);
    }

    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = this.getStatus(request);
        if (status == HttpStatus.NO_CONTENT) {
            return new ResponseEntity(status);
        } else {
            Map<String, Object> body = this.getErrorAttributes(request, this.getErrorAttributeOptions(request, MediaType.ALL));
            return new ResponseEntity(body, status);
        }
    }
}
```
BasicErrorController의 코드를 보면 이전에 내가 직접 적은 코드와 똑같은 형식으로 만들어져있는걸 확인할 수 있다.<br>
한가지 확인할 점은 클라이언트 요청의 Accept 해더 값이 `text/html` 인 경우에는 `errorHtml()` 을 호출해서 view를 제공한다는 점이다.<br>
또한 스프링 부트의 기본 설정은 오류 발생시 `/error` 를 오류 페이지로 요청한다.<br>
`BasicErrorController` 는 이 경로를 기본으로 받는다. ( `server.error.path` 로 수정 가능, 기본 경로 `/ error` )<br>

**Postman으로 실행한 BasicErrorController API 예외처리 값** 👨🏻‍💻<br>
```
{
"timestamp": "2024-02-04T04:18:35.264+00:00",
"status": 500,
"error": "Internal Server Error",
"path": "/api/members/ex"
}
```

`BasicErrorController` 를 확장하면 JSON 메시지도 변경할 수 있다.<br>
하지만 `@ExceptionHandler` 가 제공하는 기능을 사용하는 것이 더 나은 방법이므로 지금은 `BasicErrorController` 를 확장해서 JSON 오류 메시지를 변경할 수 있다 정도로만 알아두자!<br>
스프링 부트가 제공하는 `BasicErrorController` 는 HTML 페이지를 제공하는 경우에는 매우 편리하다.<br>
하지만 API의 예외처리는 API 마다, 각각의 컨트롤러나 예외마다 서로 다른 응답 결과를 출력해야 할 수도 있다. <br>
예를 들어서 회원과 관련된 API에서 예외가 발생할 때 응답과, 상품과 관련된 API에서 발생하는 예외에 따라 그 결과가 달라질 수 있다. 결과적으로 매우 세밀하고 복잡하다. <br>
따라서 `BasicErrorController`는 HTML 화면을 처리할 때 사용하고, API 오류 처리는 `@ExceptionHandler` 를 사용하는 것이 좋다.<br>







