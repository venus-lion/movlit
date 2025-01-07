package movlit.be.movie.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.exception.NotExistMovieHeartByMember;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.repository.MovieRepository;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import movlit.be.movie_heart.domain.MovieHeart;
import movlit.be.movie_heart.domain.repository.MovieHeartRepository;
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
    private final MovieHeartRepository movieHeartRepository;

    public MovieListResponseDto getMoviePopular(int page, int pageSize) {
        Long MIN_VOTE_COUNT = 500L;     // 인기순위 최소 vote_count

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        List<Movie> movieList = movieRepository.findByVoteCountGreaterThan500OrderByPopularityDesc(MIN_VOTE_COUNT, pageable);

        return new MovieListResponseDto(movieList);
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

    @Transactional(readOnly = true)
    public MovieListResponseDto getMovieUserInterestByGenre(MemberId currentMemberId, int page, int pageSize) {
        // 로그인 유저의 취향 장르 3개 가져오기
        List<Genre> movieGenreList = memberGenreRepository.findUserInterestGenreList(currentMemberId);
        if(movieGenreList.isEmpty()){
            throw new MemberGenreNotFoundException();
        }
        log.info("====movieGenreList : {}", movieGenreList);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        // elasticsearch에서 가져오기
        List<Movie> movieList = movieSearchRepository.searchByUserInterestGenre(movieGenreList, pageable);
        return new MovieListResponseDto(movieList);
    }

    @Transactional(readOnly = true)
    public MovieListResponseDto getMovieByUserRecentHeart(MemberId currentMemberId, int page, int pageSize) {
        // 로그인 유저의 가장 최근 찜한 영화 가져오기

        List<Movie> movieList;

        try {
            MovieHeart lastMovieHeart = movieHeartRepository.findMostRecentMovieHeart(currentMemberId);
            Long movieId = lastMovieHeart.getMovieId();
            Movie movie = movieRepository.findByIdWithCrew(movieId);

            Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

            movieList = movieSearchRepository.searchByUserHeartMovie(movie, pageable);

        } catch (NotExistMovieHeartByMember e) {
            // 찜한 영화가 아직 없을 때
            return null;
        }

        return new MovieListResponseDto(movieList);
    }

}
