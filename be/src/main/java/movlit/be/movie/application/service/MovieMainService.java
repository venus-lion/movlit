package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.repository.MovieRepository;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieMainService {

    private final MovieRepository movieRepository;
    private final MemberGenreRepository memberGenreRepository;
    private final MovieSearchRepository movieSearchRepository;

    @Transactional(readOnly = true)
    public MovieListResponseDto getMoviePopular(int page, int pageSize) {
        Long MIN_VOTE_COUNT = 500L;     // 인기순위 최소 vote_count

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        List<Movie> movieList = movieRepository.findByVoteCountGreaterThan500OrderByPopularityDesc(MIN_VOTE_COUNT,
                pageable);

        return new MovieListResponseDto(movieList);
    }

    @Transactional(readOnly = true)
    public MovieListResponseDto getMovieLatest(int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        return new MovieListResponseDto(movieRepository.findAllOrderByReleaseDateDesc(pageable));
    }

    @Transactional(readOnly = true)
    public MovieListByGenreResponseDto getMovieGroupbyGenre(Long genreId, int page, int pageSize) {
        // genreId -> Genre Enum객체
        Genre genre = Genre.of(genreId);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        List<Movie> movieList = movieRepository.findByMovieGenreIdForEntity_GenreId(genre.getId(), pageable);

        MovieListByGenreResponseDto responseDto = new MovieListByGenreResponseDto(genreId, genre.getName(), movieList);
        return responseDto;
    }

}
