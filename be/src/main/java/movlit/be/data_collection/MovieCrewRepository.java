package movlit.be.data_collection;

import movlit.be.MovieCrew;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCrewRepository extends JpaRepository<MovieCrew, Long> {
}
