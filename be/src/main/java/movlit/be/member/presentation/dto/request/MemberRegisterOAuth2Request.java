package movlit.be.member.presentation.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberRegisterOAuth2Request {

    private String email;
    private String password;
    private String dob;
    private String profileImgUrl;

    @Builder
    public MemberRegisterOAuth2Request(String email, String password,
                                       String dob, String profileImgUrl) {
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.profileImgUrl = profileImgUrl;
    }

}
