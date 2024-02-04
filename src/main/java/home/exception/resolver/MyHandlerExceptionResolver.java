package home.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
                /**
                 * resolveException에서 예외를 먹은 후, response.sendError로 상태 코드를 변경해준다.
                 * return new ModelAndView(); -> 정상 반환이라는 의미
                 */
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
        /**
         * return null; -> 다음 ExceptionResolver 를 찾아서 실행한다.
         *                 만약 처리할 수 있는 ExceptionResolver 가 없으면 예외 처리가 안되고,
         *                 기존에 발생한 예외를 서블릿 밖으로 던진다.
         */
    }
}
