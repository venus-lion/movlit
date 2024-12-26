package movlit.be.security;

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
    public UserDetails loadUserByUsername(String Membername) throws UsernameNotFoundException {
        Member member = memberService.findByMemberId(Membername);

        if (member == null) {
            log.warn("Login 실패: 아이디를 찾을 수 없습니다. (Membername: " + Membername + ")");
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + Membername);
        }

        log.info("Login 시도: " + member.getMemberId());
        return new MyMemberDetails(member);
    }

}
