package movlit.be.member.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import movlit.be.member.infra.persistence.jpa.MemberGenreJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberGenreRepositoryImpl implements MemberGenreRepository {
    private final MemberGenreJpaRepository memberGenreJpaRepository;
    @Override
    public List<Long> findGenreIdsByMemberId(MemberId memberId) {
        List<Long> genresByMemberId = memberGenreJpaRepository.findGenresByMemberId(memberId);

        if (genresByMemberId.isEmpty()){
            throw new MemberGenreNotFoundException();
        }
        return genresByMemberId;
    }

}
