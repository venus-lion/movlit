package movlit.be.member.presentation.dto.request;

import lombok.Getter;
import movlit.be.common.annotation.PasswordMatch;

@Getter
@PasswordMatch
public class MemberLoginRequest {

    private String email;
    private String password;

}
