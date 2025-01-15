package movlit.be.movie.application.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.NotExistMovieHeartByMember;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberGenreService;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieCrewResponseDto;
import movlit.be.movie.presentation.dto.response.MovieDocumentResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import movlit.be.movie_heart.application.service.MovieHeartService;
import movlit.be.movie_heart.domain.MovieHeart;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieSearchService {

    private final MemberGenreService memberGenreService;
    private final MovieHeartService movieHeartService;
    private final MovieReadService movieReadService;
    private final MovieCrewReadServiceMyk movieCrewReadServiceMyk;
    private final MovieSearchRepository movieSearchRepository;

    @Transactional(readOnly = true)
    public MovieListResponseDto searchMovieByMemberInterestGenre(MemberId currentMemberId, int page, int pageSize) {
        // 로그인 유저의 취향 장르 3개 가져오기
        List<Genre> movieGenreList = memberGenreService.fetchMemberInterestGenre(currentMemberId);

        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        // elasticsearch에서 가져오기
        List<Movie> movieList = movieSearchRepository.searchMovieByMemberInterestGenre(movieGenreList, pageable);
        return new MovieListResponseDto(movieList);
    }

    public MovieListResponseDto fetchMovieByMemberRecentHeart(MemberId currentMemberId, int page, int pageSize) {
        List<Movie> movieList;

        try {
            // 로그인 유저의 가장 최근 찜한 영화 가져오기
            List<MovieHeart> movieHeartList = movieHeartService.fetchMovieHeartRecentByMember(currentMemberId);
            List<Long> movieIds = movieHeartList.stream().map(MovieHeart::getMovieId).collect(Collectors.toList());

            List<MovieCrewResponseDto> heartedMovieCrewList = movieCrewReadServiceMyk.fetchMovieCrewByMovieId(movieIds);
            Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

            movieList = movieSearchRepository.searchMovieByMemberHeartCrew(heartedMovieCrewList, pageable);
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
