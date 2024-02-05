### DefaultHandlerExceptionResolver는 아래와 같이 여러 오류에 대해 미리 정의가 되어있다❗️
```java
@Nullable
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        try {
            if (ex instanceof HttpRequestMethodNotSupportedException) {
                return this.handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException)ex, request, response, handler);
            }

            if (ex instanceof HttpMediaTypeNotSupportedException) {
                return this.handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException)ex, request, response, handler);
            }

            if (ex instanceof HttpMediaTypeNotAcceptableException) {
                return this.handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException)ex, request, response, handler);
            }

            if (ex instanceof MissingPathVariableException) {
                return this.handleMissingPathVariable((MissingPathVariableException)ex, request, response, handler);
            }

            if (ex instanceof MissingServletRequestParameterException) {
                return this.handleMissingServletRequestParameter((MissingServletRequestParameterException)ex, request, response, handler);
            }

            if (ex instanceof ServletRequestBindingException) {
                return this.handleServletRequestBindingException((ServletRequestBindingException)ex, request, response, handler);
            }

            if (ex instanceof ConversionNotSupportedException) {
                return this.handleConversionNotSupported((ConversionNotSupportedException)ex, request, response, handler);
            }
            
            // TypeMismatchException 에러일 경우
            if (ex instanceof TypeMismatchException) {
                return this.handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
            }

            if (ex instanceof HttpMessageNotReadableException) {
                return this.handleHttpMessageNotReadable((HttpMessageNotReadableException)ex, request, response, handler);
            }

            if (ex instanceof HttpMessageNotWritableException) {
                return this.handleHttpMessageNotWritable((HttpMessageNotWritableException)ex, request, response, handler);
            }

            if (ex instanceof MethodArgumentNotValidException) {
                return this.handleMethodArgumentNotValidException((MethodArgumentNotValidException)ex, request, response, handler);
            }

            if (ex instanceof MissingServletRequestPartException) {
                return this.handleMissingServletRequestPartException((MissingServletRequestPartException)ex, request, response, handler);
            }

            if (ex instanceof BindException) {
                return this.handleBindException((BindException)ex, request, response, handler);
            }

            if (ex instanceof NoHandlerFoundException) {
                return this.handleNoHandlerFoundException((NoHandlerFoundException)ex, request, response, handler);
            }

            if (ex instanceof AsyncRequestTimeoutException) {
                return this.handleAsyncRequestTimeoutException((AsyncRequestTimeoutException)ex, request, response, handler);
            }
        } catch (Exception var6) {
            if (this.logger.isWarnEnabled()) {
                this.logger.warn("Failure while trying to resolve exception [" + ex.getClass().getName() + "]", var6);
            }
        }

        return null;
    }
```
```java
   protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
        response.sendError(400);
        return new ModelAndView();
    }
```
### TypeMismatchException 에러일 경우 response.sendError()로 상태 코드를 400으로 변경하는 것을 볼 수 있다 ❗️

