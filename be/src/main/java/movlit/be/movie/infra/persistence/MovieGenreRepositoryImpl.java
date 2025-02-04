package movlit.be.movie.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.Genre;
import movlit.be.movie.domain.repository.MovieGenreRepository;
import movlit.be.movie.infra.persistence.jpa.MovieGenreJpaRepository;
import movlit.be.movie.presentation.dto.response.MovieDetailGenreResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MovieGenreRepositoryImpl implements MovieGenreRepository {

    private final MovieGenreJpaRepository movieGenreJpaRepository;

    @Override
    public List<MovieDetailGenreResponse> fetchMovieDetailGenreNamesByMovieId(Long movieId) {
        return movieGenreJpaRepository.findGenreIdsByMovieId(movieId)
                .stream()
                .map(genreId -> new MovieDetailGenreResponse(Genre.of(genreId).getName()))
                .toList();
    }

    @Override
    public List<Genre> fetchMovieDetailGenresByMovieId(Long movieId) {
        return movieGenreJpaRepository.findGenreIdsByMovieId(movieId)
                .stream()
                .map(Genre::of)
                .toList();
    }

}
