package movlit.be.movie.infra.persistence;

import movlit.be.movie.domain.entity.MovieGenreEntity;
import movlit.be.movie.domain.entity.MovieGenreIdForEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreJpaRepository extends JpaRepository<MovieGenreEntity, MovieGenreIdForEntity> {
    Page<MovieGenreEntity> findByMovieGenreIdForEntity_GenreIdOrderByMovieEntity_ReleaseDateDesc(Long genreId, Pageable pageable);
}
