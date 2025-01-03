package movlit.be.auth.application.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokenIssueResponse {

    private String accessToken;
    private String refreshToken;

}
