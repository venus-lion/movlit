package movlit.be.movie.domain;

import lombok.Getter;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.entity.MovieGenreIdForEntity;

@Getter
public class MovieGenre {

    private MovieGenreIdForEntity movieGenreId;
    private MovieEntity movieEntity;

}
