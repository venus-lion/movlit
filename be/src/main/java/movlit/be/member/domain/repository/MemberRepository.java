package movlit.be.member.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    void deleteById(MemberId id);

    Member findByEmail(String email);

    Member findById(MemberId memberId);

}
