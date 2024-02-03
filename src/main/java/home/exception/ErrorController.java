package home.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION =
            "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE =
            "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI =
            "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME =
            "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE =
            "javax.servlet.error.status_code";

    @RequestMapping("/error-page/page404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage404");
        return "error-page/errorPage404";
    }

    @RequestMapping("/error-page/page500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage500");
        return "error-page/errorPage500";
    }

    /**
     * produces = MediaType.APPLICATION_JSON_VALUE로 API 요청인지 구분한다.
     *
     * Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
     *         result.put("status",request.getAttribute(ERROR_STATUS_CODE));
     *         result.put("message",ex.getMessage());
     * 서버가 request안에 담아주는 정보들
     *`javax.servlet.error.exception` : 예외
     *`javax.servlet.error.exception_type` : 예외 타입
     *`javax.servlet.error.message` : 오류 메시지
     *`javax.servlet.error.request_uri` : 클라이언트 요청 URI `
     *`javax.servlet.error.servlet_name` : 오류가 발생한 서블릿 이름 `
     *`javax.servlet.error.status_code` : HTTP 상태 코드

     */
    @RequestMapping(value = "/error-page/page500",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> errorPage500Api(HttpServletRequest request,
                                                              HttpServletResponse response){
        log.info("API errorPage 500");
        Map<String,Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status",request.getAttribute(ERROR_STATUS_CODE));
        result.put("message",ex.getMessage());
        Integer statusCode = (Integer)
                request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
    }
}
