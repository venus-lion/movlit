package movlit.be.member.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import movlit.be.member.infra.persistence.jpa.MemberGenreJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberGenreService {
    private final MemberGenreRepository memberGenreRepository;

    public List<Genre> fetchUserInterestGenreList(MemberId memberId){
        List<Genre> genres = memberGenreRepository.findUserInterestGenreList(memberId);
        return genres;
    }


}
