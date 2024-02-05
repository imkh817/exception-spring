## 스프링 부트가 기본으로 제공하는 `ExceptionResolver` 는 다음과 같다.
`HandlerExceptionResolverComposite` 에 다음 순서로 등록되어 있다.

1. `ExceptionHandlerExceptionResolver`
2. `ResponseStatusExceptionResolver`
3. `DefaultHandlerExceptionResolver` -> 우선순위가가장낮다.