package movlit.be.member.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
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

    @NotBlank(message = "생년월일은 공백일 수 없습니다.")
    private String dob;

    @Size(min = 3, message = "장르는 최소 3개 이상 선택해야 합니다.")
    private List<Long> genreIds;

    @Builder
    public MemberRegisterRequest(String nickname, String email, String password, String repeatPassword, String dob,
                                 List<Long> genreIds) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.dob = dob;
        this.genreIds = genreIds;
    }

}
