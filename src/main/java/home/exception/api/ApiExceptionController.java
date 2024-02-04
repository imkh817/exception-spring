package home.exception.api;

import home.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @Data
    @AllArgsConstructor
    static class MemberDTO{
        private String MemberId;
        private String name;
    }
}
