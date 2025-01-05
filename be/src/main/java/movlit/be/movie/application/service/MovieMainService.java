package movlit.be.movie.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.NotExistMovieHeartByMember;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.MovieHeart;
import movlit.be.movie.domain.repository.MovieHeartRepository;
import movlit.be.movie.domain.repository.MovieRepository;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieListByGenreResponseDto;
import movlit.be.movie.presentation.dto.response.MovieListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final MovieRepository movieRepository;
    private final MemberRepository memberRepository;
    private final MovieSearchRepository movieSearchRepository;
    private final MovieHeartRepository movieHeartRepository;

    public MovieListResponseDto getMoviePopular(int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        List<Movie> movieList = movieRepository.findAllOrderByHeartCountDescVoteCountDescPopularityDesc(pageable);

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

    public MovieListResponseDto getMovieUserInterestByGenre(MemberId currentMemberId, int page, int pageSize) {
        List<Genre> movieGenreList = memberRepository.findUserInterestGenreList(currentMemberId);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        List<Movie> movieList = movieSearchRepository.searchByUserInterestGenre(movieGenreList, pageable);
        return new MovieListResponseDto(movieList);
    }


    public MovieListResponseDto getMovieByUserRecentHeart(MemberId currentMemberId) {
        // 로그인 유저의 가장 최근 찜한 영화 가져오기

        List<Movie> movieList;

        try{
            MovieHeart lastMovieHeart = movieHeartRepository.findMostRecentMovieHeart(currentMemberId);
            Long movieId = lastMovieHeart.getMovieId();

            // 1. crew 가져와서 elasticsearch에 movieCrew로 검색


            // 2.


            // 3. keyword 가져와서 elasticsearch에 overview로 검색 (morelikethis쿼리)


            // 4. tagline 가져와서 elasticsearch에 overview로 검색 (morelikethis쿼리)

        } catch (NotExistMovieHeartByMember e){
            // 찜한 영화가 아직 없을 때
        }




        return null;
    }
}
