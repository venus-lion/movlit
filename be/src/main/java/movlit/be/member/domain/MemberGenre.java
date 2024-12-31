package movlit.be.member.domain;

import lombok.Getter;
import movlit.be.movie.domain.MovieGenreId;
import movlit.be.movie.domain.entity.MovieEntity;

@Getter
public class MemberGenre {

    private MovieGenreId movieGenreId;
    private MovieEntity movieEntity;

}
