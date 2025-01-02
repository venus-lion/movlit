package movlit.be.data_collection.movie.jpa;

import movlit.be.movie.domain.entity.MovieTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTagRepository extends JpaRepository<MovieTagEntity, Long> {

}
