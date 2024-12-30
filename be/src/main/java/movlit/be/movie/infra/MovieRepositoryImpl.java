package movlit.be.movie.infra;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.movie.application.converter.main.MovieConverter;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.repository.MovieRepository;
import movlit.be.movie.infra.persistence.MovieJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieJpaRepository movieJpaRepository;

    @Override
    public Movie save(Movie movie) {
        MovieEntity movieEntity = movieJpaRepository.save(MovieConverter.toEntity(movie));
        return MovieConverter.toDomain(movieEntity);
    }

    @Override
    public Movie findById(Long movieId) {
        MovieEntity movieEntity = movieJpaRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
        return MovieConverter.toDomain(movieEntity);
    }

    @Override
    public void deleteById(Long movieId) {
        movieJpaRepository.deleteById(movieId);
    }

    @Override
    public List<Movie> findAllOrderByReleaseDateDesc(Pageable pageable) {
        Page<MovieEntity> movieEntityPage = movieJpaRepository.findAllByOrderByReleaseDateDesc(pageable);
        return movieEntityPage.getContent().stream().map(MovieConverter::toDomain).toList();
    }

    @Override
    public List<Movie> findAllOrderByHeartCountDescVoteCountDescPopularityDesc(Pageable pageable) {
        Page<MovieEntity> movieEntityPage = movieJpaRepository.findAllByOrderByHeartCountDescVoteCountDescPopularityDesc(pageable);

        return movieEntityPage.getContent().stream().map(MovieConverter::toDomain).toList();
    }

}
