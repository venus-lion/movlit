package movlit.be.new_auth.application.usecase;

import lombok.RequiredArgsConstructor;
import movlit.be.new_auth.application.service.TokenService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogoutUseCase {

    private final TokenService tokenService;

    public void logout(String accessToken) {
        tokenService.revoke(accessToken);
    }

}
