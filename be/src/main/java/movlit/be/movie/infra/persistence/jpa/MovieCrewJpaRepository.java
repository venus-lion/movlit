package movlit.be.movie.infra.persistence.jpa;

import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.movie.domain.entity.MovieCrewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCrewJpaRepository extends JpaRepository<MovieCrewEntity, MovieCrewId> {

}
