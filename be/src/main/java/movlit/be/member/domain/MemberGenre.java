package movlit.be.member.domain;

import lombok.Getter;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.entity.MovieGenreIdEntity;

@Getter
public class MemberGenre {

    private MovieGenreIdEntity movieGenreIdEntity;
    private MovieEntity movieEntity;

}
