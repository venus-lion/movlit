package movlit.be.member.domain.repository;

import movlit.be.member.domain.Member;

public interface MemberRepository {

    Member save(Member member);

    Member findById(String id);

    void deleteById(String id);

}
