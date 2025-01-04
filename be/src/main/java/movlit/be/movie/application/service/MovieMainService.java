package movlit.be.movie.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.repository.MovieRepository;
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

    public List<Movie> getMovieUserInterestByGenre(MemberId currentMemberId, int page, int pageSize) {
        List<Genre> movieGenreList = memberRepository.findUserInterestGenreList(currentMemberId);


        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder(); // BoolQuery 생성에 사용할 빌더 생성

        for(Genre genre : movieGenreList) {
            boolQueryBuilder.should(
                    TermQuery.of(t -> t.field("movieGenre.genreName").value(genre.getName()))._toQuery()
            );
        }


        return List.of();
    }


    public MovieListResponseDto getMovieByUserRecentHeart(MemberId currentMemberId) {
        return null;
    }
}
