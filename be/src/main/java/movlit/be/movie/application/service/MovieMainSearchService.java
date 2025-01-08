package movlit.be.movie.application.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.exception.NotExistMovieHeartByMember;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.repository.MovieRepository;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieDocumentResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import movlit.be.movie_heart.domain.MovieHeart;
import movlit.be.movie_heart.domain.repository.MovieHeartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieMainSearchService {

    private final MovieRepository movieRepository;
    private final MemberGenreRepository memberGenreRepository;
    private final MovieSearchRepository movieSearchRepository;
    private final MovieHeartRepository movieHeartRepository;

    @Transactional(readOnly = true)
    public MovieListResponseDto getMovieUserInterestByGenre(MemberId currentMemberId, int page, int pageSize) {
        // 로그인 유저의 취향 장르 3개 가져오기
        List<Genre> movieGenreList = memberGenreRepository.findUserInterestGenreList(currentMemberId);
        if (movieGenreList.isEmpty()) {
            throw new MemberGenreNotFoundException();
        }
        log.info("====movieGenreList : {}", movieGenreList);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        // elasticsearch에서 가져오기
        List<Movie> movieList = movieSearchRepository.searchInterestGenre(movieGenreList, pageable);
        return new MovieListResponseDto(movieList);
    }

    @Transactional(readOnly = true)
    public MovieListResponseDto getMovieByUserRecentHeart(MemberId currentMemberId, int page, int pageSize) {
        // 로그인 유저의 가장 최근 찜한 영화 가져오기

        List<Movie> movieList;

        try {
            List<MovieHeart> lastMovieHeart = movieHeartRepository.findMostRecentMovieHeart(currentMemberId);
            List<Long> movieIds = lastMovieHeart.stream().map(MovieHeart::getMovieId).collect(Collectors.toList());

            List<Movie> heartedMovieList = movieRepository.findByIdWithCrewIn(movieIds);

            Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

            movieList = movieSearchRepository.searchByUserHeartMovieAndCrew(heartedMovieList, pageable);
            return new MovieListResponseDto(movieList);

        } catch (NotExistMovieHeartByMember e) {
            // 찜한 영화가 아직 없을 때
            // 일단은 빈 리스트 처리
            return new MovieListResponseDto(Collections.emptyList());
        }
    }

    @Transactional(readOnly = true)
    public MovieDocumentResponseDto getSearchMovie(String inputStr, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        return movieSearchRepository.searchMovieList(inputStr, pageable);
    }
}
