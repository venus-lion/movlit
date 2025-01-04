package movlit.be.member.domain.repository;

import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.MemberGenre;

import java.util.List;

public interface MemberRepository {

    Member save(Member member);

    void deleteById(MemberId id);

    Member findByEmail(String email);

    Member findById(MemberId memberId);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    List<Genre> findUserInterestGenreList(MemberId memberId);
}
