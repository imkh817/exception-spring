package home.exception.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /**
         * ServletRequest servletRequest -> 다른 프로토콜도 받을 수 있는 클래스
         * HttpServletRequest의 상위 클래스이다.
         */
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();
        try{
            log.info("REQUEST [{}],[{}],[{}]",uuid,httpServletRequest.getDispatcherType(),requestURI);
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}][{}]", uuid, servletRequest.getDispatcherType(),
                    requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
