package movlit.be.new_auth.application.usecase;

import java.util.Date;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.ExpiredTokenException;
import movlit.be.common.exception.InvalidTokenException;
import movlit.be.common.util.IdFactory;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.new_auth.application.dto.request.TokenIssueRequest;
import movlit.be.new_auth.application.dto.response.TokenIssueResponse;
import movlit.be.new_auth.application.service.TokenService;
import movlit.be.new_auth.domain.RefreshTokenStorage;
import movlit.be.new_auth.infra.util.JwtUtil;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenReissueUseCase {

    private final JwtUtil jwtUtil;
    private final RefreshTokenStorage refreshTokenStorage;
    private final TokenService tokenService;
    private final MemberReadService memberReadService;

    public TokenIssueResponse reIssueToken(TokenIssueRequest request) {
        String refreshToken = request.getRefreshToken();
        String memberId = jwtUtil.parseRefreshToken(refreshToken);
        try {
            validateRefreshToken(refreshToken, memberId);
            Member member = memberReadService.findByMemberId(IdFactory.createMemberId(memberId));
            Date now = new Date();
            return tokenService.issue(now, member);
        } catch (ExpiredTokenException e) {
            // 자동 로그아웃 처리 (refreshToken 삭제)
            refreshTokenStorage.deleteByMemberId(memberId);
            // 클라이언트에게 자동 로그아웃 알림
            throw new ExpiredTokenException();
        }
    }

    private void validateRefreshToken(String refreshToken, String memberId) {
        String stored = refreshTokenStorage.findByMemberId(memberId);
        if (!Objects.equals(refreshToken, stored)) {
            throw new InvalidTokenException();
        }
    }

}
