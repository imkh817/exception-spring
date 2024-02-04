package home.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// 서블릿 오류 페이지 등록
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    /**
    WebServerFactoryCustomizer 인터페이스는 내장된 웹 서버를 커스터마이징하기 위해 사용된다.
    스프링 부트는 내장된 웹 서버를 사용하여 애플리케이션을 실행할 수 있으며,
    WebServerFactoryCustomizer를 통해 이 웹 서버의 구성을 조정할 수 있다.

    public interface WebServerFactoryCustomizer<T extends ConfigurableWebServerFactory> {
        void customize(T factory);
    }
    여기서 T는 ConfigurableWebServerFactory를 구현한 구체적인 내장 웹 서버 팩토리 클래스를 나타낸다.
    이 팩토리는 내장된 웹 서버의 구성을 다루는 데 사용됩니다.
    **/
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        /**
         * 에러페이지 생성-> 해당하는 에러발생시 정해준 path로 이동
         */
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,"/error-page/page404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/error-page/page500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class,"/error-page/page500");

        /**
         *  customize 메서드를 사용하여 내장된 웹 서버 팩토리의 에러 페이지를 설정
         */
        factory.addErrorPages(errorPage404,errorPage500,errorPageEx);
    }
}
