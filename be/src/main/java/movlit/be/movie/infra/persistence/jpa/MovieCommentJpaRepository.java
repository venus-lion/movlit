package movlit.be.movie.infra.persistence.jpa;

import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieCommentJpaRepository extends JpaRepository<MovieCommentEntity, MovieCommentId> {

}
