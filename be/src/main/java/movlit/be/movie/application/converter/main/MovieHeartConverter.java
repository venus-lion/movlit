package movlit.be.movie.application.converter.main;

import movlit.be.movie.domain.MovieHeart;
import movlit.be.movie.domain.entity.MovieHeartEntity;

public class MovieHeartConverter {

    public static MovieHeart toDomain(MovieHeartEntity movieHeartEntity){
        return MovieHeart.builder()
                .movieHeartId(movieHeartEntity.getMovieHeartId())
                .movieId(movieHeartEntity.getMovieId())
                .memberId(movieHeartEntity.getMemberId())
                .regDt(movieHeartEntity.getRegDt())
                .build();
    }
}
