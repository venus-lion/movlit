package movlit.be.member.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberGenreService {

    private final MemberGenreRepository memberGenreRepository;

    @Transactional(readOnly = true)
    public List<Genre> fetchUserInterestGenreList(MemberId memberId) {
        List<Genre> genres = memberGenreRepository.findUserInterestGenreList(memberId);
        return genres;
    }

    @Transactional(readOnly = true)
    public List<Genre> fetchMemberInterestGenre(MemberId memberId) {
        List<Genre> movieGenreList = memberGenreRepository.findUserInterestGenreList(memberId);
        if (movieGenreList.isEmpty()) {
            throw new MemberGenreNotFoundException();
        }

        return movieGenreList;
    }

}
