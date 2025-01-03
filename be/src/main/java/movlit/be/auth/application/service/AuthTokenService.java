package movlit.be.auth.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.dto.AuthTokenIssueResponse;
import movlit.be.auth.domain.repository.RefreshTokenStorage;
import movlit.be.common.util.JwtTokenUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenStorage refreshTokenStorage;

    public AuthTokenIssueResponse issue(String email) {
        String accessToken = jwtTokenUtil.generateAccessToken(email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(email);
        refreshTokenStorage.saveRefreshToken(email, refreshToken);
        return new AuthTokenIssueResponse(accessToken, refreshToken);
    }

    public void revoke(String accessToken) {
        long exp = jwtTokenUtil.extractExpirationAsLong(accessToken);
        refreshTokenStorage.addBlacklist(accessToken, exp);
    }

}
