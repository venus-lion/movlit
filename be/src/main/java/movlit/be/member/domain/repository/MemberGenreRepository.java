package movlit.be.member.domain.repository;

import java.util.List;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.presentation.dto.response.GenreListReadResponse;

public interface MemberGenreRepository {

    List<Long> findGenreIdsByMemberId(MemberId memberId);

    List<Genre> findUserInterestGenreList(MemberId memberId);

}
