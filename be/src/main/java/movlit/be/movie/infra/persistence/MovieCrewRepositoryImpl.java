package movlit.be.movie.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieCrewNotFoundException;
import movlit.be.movie.domain.repository.MovieCrewRepository;
import movlit.be.movie.infra.persistence.jpa.MovieRCrewJpaRepository;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieCrewRepositoryImpl implements MovieCrewRepository {

    private final MovieRCrewJpaRepository movieRCrewJpaRepository;

    @Override
    public List<MovieDetailCrewResponse> fetchMovieDetailCrewsByMovieId(Long movieId) {
        List<MovieDetailCrewResponse> response = movieRCrewJpaRepository.findMovieCrewsByMovieId(movieId);

        if (response.isEmpty()) {
            throw new MovieCrewNotFoundException();
        }

        return response;
    }

}
