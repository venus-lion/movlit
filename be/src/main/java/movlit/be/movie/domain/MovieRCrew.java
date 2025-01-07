package movlit.be.movie.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MovieRCrew {

    private MovieRCrewId movieRCrewId;
    private MovieCrew movieCrew;
    private Movie movie;

}
