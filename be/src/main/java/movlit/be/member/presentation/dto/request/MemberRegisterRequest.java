package movlit.be.member.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import movlit.be.common.annotation.PasswordMatch;
import movlit.be.common.annotation.PasswordPattern;

@Getter
@PasswordMatch
public class MemberRegisterRequest {

    @NotBlank(message = "닉네임은 공백일 수 없습니다")
    private String nickname;

    @Email(message = "올바른 형식의 이메일을 입력해주세요")
    @NotBlank(message = "이메일은 공백일 수 없습니다")
    private String email;

    @PasswordPattern
    private String password;
    private String repeatPassword;

    private String dob;

}
