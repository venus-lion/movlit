package movlit.be.movie.domain.repository;

import java.util.List;
import movlit.be.common.util.Genre;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.presentation.dto.response.MovieCrewResponseDto;
import movlit.be.movie.presentation.dto.response.MovieDocumentResponseDto;
import org.springframework.data.domain.Pageable;

public interface MovieSearchRepository {

    List<Movie> searchMovieByMemberInterestGenre(List<Genre> genreList, Pageable pageable);

    List<Movie> searchMovieByMemberHeartCrew(List<MovieCrewResponseDto> dto, Pageable pageable);

    MovieDocumentResponseDto searchMovieList(String inputStr, Pageable pageable);

}
