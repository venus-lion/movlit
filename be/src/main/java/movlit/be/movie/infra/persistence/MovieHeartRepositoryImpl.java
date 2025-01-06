package movlit.be.movie.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.NotExistMovieHeartByMember;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.application.converter.main.MovieHeartConverter;
import movlit.be.movie.domain.MovieHeart;
import movlit.be.movie.domain.entity.MovieHeartEntity;
import movlit.be.movie.domain.repository.MovieHeartRepository;
import movlit.be.movie.infra.persistence.jpa.MovieHeartJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovieHeartRepositoryImpl implements MovieHeartRepository {

    private final MovieHeartJpaRepository movieHeartJpaRepository;

    @Override
    public MovieHeart findMostRecentMovieHeart(MemberId memberId) {
        return movieHeartJpaRepository.findTopByMemberIdOrderByRegDtDesc(memberId)
                .map(MovieHeartConverter::toDomain)
                .orElseThrow(NotExistMovieHeartByMember::new);
    }
}
