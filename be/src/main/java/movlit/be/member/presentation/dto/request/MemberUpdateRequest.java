package movlit.be.member.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import movlit.be.common.annotation.PasswordMatch;
import movlit.be.common.annotation.PasswordPattern;

@Getter
@PasswordMatch
public class MemberUpdateRequest {

    @NotBlank(message = "닉네임은 공백일 수 없습니다")
    private String nickname;

    @PasswordPattern
    private String password;
    private String repeatPassword;

    @NotBlank(message = "생년월일은 공백일 수 없습니다.")
    private String dob;

    @Size(min = 3, message = "장르는 최소 3개 이상 선택해야 합니다.")
    private List<Long> genreIds;

    @Builder
    public MemberUpdateRequest(String nickname, String password, String repeatPassword, String dob,
                               List<Long> genreIds) {
        this.nickname = nickname;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.dob = dob;
        this.genreIds = genreIds;
    }

}
