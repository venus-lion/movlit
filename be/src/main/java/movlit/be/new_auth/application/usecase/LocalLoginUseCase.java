package movlit.be.new_auth.application.usecase;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.new_auth.application.dto.request.LoginRequest;
import movlit.be.new_auth.application.dto.response.TokenIssueResponse;
import movlit.be.new_auth.application.service.TokenService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LocalLoginUseCase {

    private final TokenService tokenService;
    private final MemberReadService memberReadService;

    @Transactional
    public TokenIssueResponse login(LoginRequest request) {
        Member member = memberReadService.findByMemberEmail(request.getEmail());
        Date now = new Date();
        return tokenService.issue(now, member);
    }

}
