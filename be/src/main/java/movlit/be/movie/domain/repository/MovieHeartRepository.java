package movlit.be.movie.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.MovieHeart;

public interface MovieHeartRepository {

   MovieHeart findMostRecentMovieHeart(MemberId memberId);      // 유저의 가장 최근 찜한 영화
}
