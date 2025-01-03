package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.repository.MovieRepository;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final MovieRepository movieRepository;

    public MovieListResponseDto getMoviePopular(int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        return new MovieListResponseDto(movieRepository.findAllOrderByHeartCountDescVoteCountDescPopularityDesc(pageable));
    }

    public MovieListResponseDto getMovieLatest(int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        return new MovieListResponseDto(movieRepository.findAllOrderByReleaseDateDesc(pageable));
    }

    public MovieListByGenreResponseDto getMovieGroupbyGenre(Long genreId, int page, int pageSize) {
        // genreId -> Genre Enum객체
        Genre genre = Genre.of(genreId);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        List<Movie> movieList = movieRepository.findByMovieGenreIdForEntity_GenreId(genre.getId(), pageable);

        MovieListByGenreResponseDto responseDto = new MovieListByGenreResponseDto(genreId, genre.getName(), movieList);
        return responseDto;
    }

    public List<Movie> getMovieUserInterestByGenre(MemberId memberId, int page, int pageSize) {
        return List.of();
    }

}
