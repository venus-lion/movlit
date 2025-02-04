package movlit.be.movie.presentation.dto.response;

import java.util.List;
import movlit.be.movie.domain.Movie;

public record MovieListResponseDto(List<Movie> movieList) {

}
