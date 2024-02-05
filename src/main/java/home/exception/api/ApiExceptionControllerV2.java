package home.exception.api;

import home.exception.exception.UserException;
import home.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionControllerV2 {

    /**
     IllegalArgumentException이 발생할 경우, 해당 메서드가 호출되어 HTTP 응답 상태 코드를 400 Bad Request로 설정한다.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExHandle(IllegalArgumentException e){
        /**
         * 만약 @ResponseStatus(HttpStatus.XXX) 어노테이션을 추가로 사용하지 않는다면,
         * 기본적으로 상태 코드는 200(OK)으로 설정된다. 이는 자세히 생각해보면 당연히 소리이다.
         * 왜냐하면 @ExceptionHandelr로 예외를 잡고 , 클라이언트에게 정상적인 응답을 보내주기 때문이다.
         * WAS는 예외를 처리했지만, 정상적인 호출로 간주하여 상태 코드를 200으로 설정한다.
         */

        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD REQUEST",e.getMessage());
    }

    @ExceptionHandler // 애노테이션의 에러타입을 생략하면 메서드 매개변수의 에러타입이 자동으로 등록된다.
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("User-Ex",e.getMessage());
        return new ResponseEntity<>(errorResult,HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e){
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX" , "내부 오류");
    }

    @RequestMapping("/api2/members/{id}")
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

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String MemberId;
        private String name;
    }
}
