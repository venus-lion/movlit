package movlit.be.auth.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.dto.AuthTokenIssueResponse;
import movlit.be.auth.domain.repository.RefreshTokenStorage;
import movlit.be.common.exception.TokenNotFoundException;
import movlit.be.common.util.JwtTokenUtil;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenService {

    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenStorage refreshTokenStorage;

    public AuthTokenIssueResponse issue(Member member) {
        String id = member.getMemberId().getValue();
        String email = member.getEmail();

        String accessToken = jwtTokenUtil.generateAccessToken(id, email);
        String refreshToken = jwtTokenUtil.generateRefreshToken(id, email);
        refreshTokenStorage.saveRefreshToken(email, refreshToken);
        return new AuthTokenIssueResponse(accessToken, refreshToken);
    }

    public void revoke(String accessToken) {
        long exp = jwtTokenUtil.extractExpirationAsLong(accessToken);
        refreshTokenStorage.addBlacklist(accessToken, exp);
    }

    public void validateNotBlacklisted(String token) {
        if (refreshTokenStorage.isBlacklist(token)) {
            throw new TokenNotFoundException();
        }
    }

}
