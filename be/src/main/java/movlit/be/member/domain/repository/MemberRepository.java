package movlit.be.member.domain.repository;

import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.MemberGenre;

import java.util.List;
import movlit.be.member.domain.entity.MemberEntity;

public interface MemberRepository {

    MemberEntity saveEntity(MemberEntity memberEntity);

    Member save(Member member);

    void deleteById(MemberId id);

    Member findByEmail(String email);

    Member findById(MemberId memberId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existByMemberId(MemberId memberId);

    List<Genre> findUserInterestGenreList(MemberId memberId);
}
