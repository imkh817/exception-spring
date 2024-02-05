```java
@Nullable
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex) {
        try {
            if (ex instanceof ResponseStatusException) {
                return this.resolveResponseStatusException((ResponseStatusException)ex, request, response, handler);
            }
            // @ResonseStatus 애노테이션이 붙은 것들 확인
            ResponseStatus status = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(ex.getClass(), ResponseStatus.class);
            
            if (status != null) {
                return this.resolveResponseStatus(status, request, response, handler, ex);
            }

            if (ex.getCause() instanceof Exception) {
                return this.doResolveException(request, response, handler, (Exception)ex.getCause());
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
   protected ModelAndView applyStatusAndReason(int statusCode, @Nullable String reason, HttpServletResponse response) throws IOException {
    /**
     *  결국 우리가 전에 구현했듯이 ResponseStatusExceptionResolver도 reponse.sendError()로 
     *  WAS에서 다시 오류 페이지( `/error` )를 내부 요청한다.
     */
   
        if (!StringUtils.hasLength(reason)) {
            response.sendError(statusCode);
        } else {
            String resolvedReason = this.messageSource != null ? this.messageSource.getMessage(reason, (Object[])null, reason, LocaleContextHolder.getLocale()) : reason;
            response.sendError(statusCode, resolvedReason);
        }

        return new ModelAndView();
    }
```