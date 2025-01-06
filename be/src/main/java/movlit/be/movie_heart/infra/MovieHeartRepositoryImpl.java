package movlit.be.movie_heart.infra;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.NotExistMovieHeartByMember;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie_heart.application.converter.MovieHeartConverter;
import movlit.be.movie_heart.domain.MovieHeart;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;
import movlit.be.movie_heart.domain.repository.MovieHeartRepository;
import movlit.be.movie_heart.infra.persistence.MovieHeartJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieHeartRepositoryImpl implements MovieHeartRepository {

    private final MovieHeartJpaRepository movieHeartJpaRepository;

    @Override
    public MovieHeartEntity heart(MovieHeartEntity movieHeartEntity) {
        return movieHeartJpaRepository.save(movieHeartEntity);
    }

    @Override
    public void deleteByMovieIdAndMemberId(Long movieId, MemberId memberId) {
        movieHeartJpaRepository.deleteByMovieIdAndMemberId(movieId, memberId);
    }

    @Override
    public boolean existsByMovieIdAndMemberId(Long movieId, MemberId memberId) {
        return movieHeartJpaRepository.existsByMovieIdAndMemberId(movieId, memberId);
    }

    @Override
    public MovieHeart findMostRecentMovieHeart(MemberId memberId) {
        return movieHeartJpaRepository.findTopByMemberIdOrderByRegDtDesc(memberId)
                .map(MovieHeartConverter::toDomain)
                .orElseThrow(NotExistMovieHeartByMember::new);
    }

}
