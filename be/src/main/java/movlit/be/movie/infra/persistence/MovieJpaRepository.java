package movlit.be.movie.infra.persistence;

import movlit.be.movie.domain.entity.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, Long> {
    Page<MovieEntity> findAllByOrderByReleaseDateDesc(Pageable pageable);      // 개봉순

    Page<MovieEntity> findAllByOrderByHeartCountDescVoteCountDescPopularityDesc(Pageable pageable);    // 인기순

}
