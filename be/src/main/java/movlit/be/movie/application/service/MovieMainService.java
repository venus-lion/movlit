package movlit.be.movie.application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.repository.MovieRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final MovieRepository movieRepository;

    public List<Movie> getMoviePopular(int page, int pageSize){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        return movieRepository.findAllOrderByHeartCountDescVoteCountDescPopularityDesc(pageable);
    }

    public List<Movie> getMovieLatest(int page, int pageSize){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);
        return movieRepository.findAllOrderByReleaseDateDesc(pageable);
    }

    public List<Movie> getMovieGroupbyGenre(Long genreId, int page, int pageSize){
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        return movieRepository.findByMovieGenreIdForEntity_GenreId(genreId, pageable);
    }

    public List<Movie> getMovieUserInterestByGenre(MemberId memberId, int page, int pageSize){
        return List.of();
    }
}
