package movlit.be.data_collection;

import movlit.be.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCollectRepository extends JpaRepository<Movie, Long> {

}
