package movlit.be.movie.presentation.dto;

import java.util.List;
import movlit.be.movie.domain.Movie;

public record MovieListByGenreResponseDto(
        Long genreId,
        String genreName,
        List<Movie> data
) {

}
