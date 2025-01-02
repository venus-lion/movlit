package movlit.be.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyMemberDetailsService implements UserDetailsService {

    private final MemberReadService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberService.findByMemberEmail(email);

        if (member == null) {
            log.warn("Login 실패: 아이디를 찾을 수 없습니다. (email: " + email + ")");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }

        log.info("Login 시도 memberId: " + member.getMemberId());
        return new MyMemberDetails(member.getMemberId());
    }

}

