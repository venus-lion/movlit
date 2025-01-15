package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.Genre;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.repository.MovieGenreRepository;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieDetailSearchService {

    private final MovieGenreRepository movieGenreRepository;
    private final MovieSearchRepository movieSearchRepository;

    public MovieListResponseDto fetchMovieDetailRelated(int page, int pageSize, Long movieId) {
        List<Genre> movieGenreList = movieGenreRepository.fetchMovieDetailGenresByMovieId(movieId);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        List<Movie> movieList = movieSearchRepository.searchMovieByMemberInterestGenre(movieGenreList, pageable);
        return new MovieListResponseDto(movieList);
    }

}
