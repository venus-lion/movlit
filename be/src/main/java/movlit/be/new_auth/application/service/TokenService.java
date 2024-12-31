package movlit.be.new_auth.application.service;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.InvalidTokenException;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.new_auth.application.dto.response.TokenIssueResponse;
import movlit.be.new_auth.domain.RefreshTokenStorage;
import movlit.be.new_auth.infra.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RefreshTokenStorage refreshTokenStorage;
    private final JwtUtil jwtUtil;
    private final MemberReadService memberReadService;

    public TokenIssueResponse issue(Date now, Member member) {
        String accessToken = jwtUtil.createAccessToken(now, member.getMemberId().getValue(), member.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(now, member.getMemberId().getValue());
        refreshTokenStorage.saveRefreshToken(member.getMemberId(), refreshToken);
        return TokenIssueResponse.of(accessToken, refreshToken);
    }

    public void revoke(String token) {
        long tokenExp = jwtUtil.extractExpiresAt(token);
        refreshTokenStorage.addBlacklist(token, tokenExp);
    }

    public void validateNotBlacklisted(String token) {
        if (refreshTokenStorage.isBlacklist(token)) {
            throw new InvalidTokenException();
        }
    }

}
