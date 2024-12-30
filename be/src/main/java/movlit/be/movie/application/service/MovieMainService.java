package movlit.be.movie.application.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.repository.MovieRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieMainService {

    private final MovieRepository movieRepository;

    public final int PAGE_SIZE = 10;

    public List<Movie> getMoviePopular(String page){
        Pageable pageable = Pageable.ofSize(PAGE_SIZE).withPage(Integer.parseInt(page) - 1);
        return movieRepository.findAllOrderByHeartCountDescVoteCountDescPopularityDesc(pageable);
    }

    public List<Movie> getMovieLatest(String page){
        Pageable pageable = Pageable.ofSize(PAGE_SIZE).withPage(Integer.parseInt(page) - 1);
        return movieRepository.findAllOrderByReleaseDateDesc(pageable);
    }
}
