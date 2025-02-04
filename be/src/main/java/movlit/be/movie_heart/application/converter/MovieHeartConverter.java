package movlit.be.movie_heart.application.converter;

import movlit.be.movie_heart.domain.MovieHeart;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;

public class MovieHeartConverter {

    // domain -> entity
    public static MovieHeartEntity toEntity(MovieHeart movieHeart) {
        return MovieHeartEntity.builder()
                .movieHeartId(movieHeart.getMovieHeartId())
                .movieId(movieHeart.getMovieId())
                .memberId(movieHeart.getMemberId())
                .isHearted(movieHeart.isHearted())
                .regDt(movieHeart.getRegDt())
                .build();
    }

    // entity -> domain
    public static MovieHeart toDomain(MovieHeartEntity movieHeartEntity) {
        return MovieHeart.builder()
                .movieHeartId(movieHeartEntity.getMovieHeartId())
                .movieId(movieHeartEntity.getMovieId())
                .memberId(movieHeartEntity.getMemberId())
                .isHearted(movieHeartEntity.isHearted())
                .regDt(movieHeartEntity.getRegDt())
                .build();
    }


}
