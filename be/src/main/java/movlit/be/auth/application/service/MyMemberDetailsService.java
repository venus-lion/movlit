package movlit.be.auth.application.service;


import lombok.extern.slf4j.Slf4j;
import movlit.be.Member;
import movlit.be.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MyMemberDetailsService implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String memberName) throws UsernameNotFoundException {
        Member member = memberService.findByMemberId(memberName);

        if (member == null) {
            log.warn("Login 실패: 아이디를 찾을 수 없습니다. (Membername: " + memberName + ")");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + memberName);
        }

        log.info("Login 시도: " + member.getMemberId());
        return new MyMemberDetails(member);
    }

}

