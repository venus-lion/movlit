package movlit.be.member.domain.repository;

import java.util.Optional;


public interface MemberRepository {
    Member save(Member memberEntity);

    Optional<Member> findById(String id);

    void deleteById(String id);
}
