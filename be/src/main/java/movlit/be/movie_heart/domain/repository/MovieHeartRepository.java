package movlit.be.movie_heart.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;

public interface MovieHeartRepository {

    boolean existsByMovieIdAndMemberId(Long movieId, MemberId memberId);

    MovieHeartEntity heart(MovieHeartEntity movieHeartEntity);

    void deleteByMovieIdAndMemberId(Long movieId, MemberId memberId);

    MovieHeartEntity findMostRecentMovieHeart(MemberId memberId);

}
