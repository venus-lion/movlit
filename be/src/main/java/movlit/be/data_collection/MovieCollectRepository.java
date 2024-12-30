package movlit.be.data_collection;

import movlit.be.movie.domain.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCollectRepository extends JpaRepository<MovieEntity, Long> {

}
