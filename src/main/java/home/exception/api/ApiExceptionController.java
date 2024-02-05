package home.exception.api;

import home.exception.exception.BadRequestException;
import home.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionController {

    @RequestMapping("/api/members/{id}")
    public MemberDTO getMember(@PathVariable("id") String id){
        log.info("API Controller");
        if (id.equals("ex")){
            throw new RuntimeException("잘못된 사용자.");
        }
        if(id.equals("bad")){
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")){
            throw new UserException("사용자 오류");
        }

        return new MemberDTO(id,"hello"+id);
    }

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2(){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"error.bad",new IllegalArgumentException());
        /**
         * ResponseStatusException(HttpStatus,@Nullable String reason,@Nullable Throwable cause);
         *
         * HttpStatus status(필수):
         * ResponseStatusException이 던져질 때 클라이언트에게 반환될 HTTP 응답의 상태 코드를 나타낸다.
         * 예를 들어, HttpStatus.BAD_REQUEST 또는 HttpStatus.NOT_FOUND와 같은 상태 코드를 설정할 수 있다.
         *
         * String reason(선택):
         * 클라이언트에게 반환될 오류 메시지의 이유를 나타낸다.
         * 이는 클라이언트가 이해할 수 있는 메시지로, 예외에 대한 자세한 설명을 제공하는 데 사용된다.
         *
         * Throwable cause(선택):
         * 예외의 원인이 되는 다른 예외를 나타낸다.
         * 예외의 원인을 전달하여 디버깅 및 로깅을 용이하게 할 수 있다.
         */
    }
    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String MemberId;
        private String name;
    }
}
