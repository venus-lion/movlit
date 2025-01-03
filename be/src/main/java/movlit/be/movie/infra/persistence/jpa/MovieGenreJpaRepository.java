package movlit.be.movie.infra.persistence.jpa;

import java.util.List;
import movlit.be.movie.domain.entity.MovieGenreEntity;
import movlit.be.movie.domain.entity.MovieGenreIdForEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MovieGenreJpaRepository extends JpaRepository<MovieGenreEntity, MovieGenreIdForEntity> {

    Page<MovieGenreEntity> findByMovieGenreIdForEntity_GenreIdOrderByMovieEntity_ReleaseDateDesc(Long genreId,
                                                                                                 Pageable pageable);

    @Query("SELECT mg.movieGenreIdForEntity.genreId "
            + "FROM MovieGenreEntity mg "
            + "WHERE mg.movieGenreIdForEntity.movieId = :movieId")
    List<Long> findGenreIdsByMovieId(Long movieId);

}
