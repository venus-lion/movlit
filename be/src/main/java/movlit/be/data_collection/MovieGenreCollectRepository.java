package movlit.be.data_collection;

import movlit.be.movie.domain.entity.MovieGenreEntity;
import movlit.be.movie.domain.entity.MovieGenreIdForEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreCollectRepository extends JpaRepository<MovieGenreEntity, MovieGenreIdForEntity> {
}
