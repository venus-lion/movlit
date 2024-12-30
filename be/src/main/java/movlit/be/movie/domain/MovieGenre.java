package movlit.be.movie.domain;

import lombok.Getter;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.entity.MovieGenreIdEntity;

@Getter
public class MovieGenre {

    private MovieGenreIdEntity movieGenreIdEntity;
    private MovieEntity movieEntity;

}
