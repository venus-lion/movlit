package movlit.be.movie.infra.persistence.jpa;

import java.util.List;
import movlit.be.movie.domain.entity.MovieRCrewEntity;
import movlit.be.movie.domain.entity.MovieRCrewIdForEntity;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieCrewJpaRepository extends JpaRepository<MovieRCrewEntity, MovieRCrewIdForEntity> {

    @Query("SELECT NEW movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse(mc.movieCrewEntity.profileImgUrl, mc.movieCrewEntity.name, mc.movieCrewEntity.charName, mc.movieCrewEntity.role) "
            + "FROM MovieRCrewEntity mc "
            + "WHERE mc.movieRCrewIdForEntity.movieId = :movieId")
    List<MovieDetailCrewResponse> findMovieCrewsByMovieId(@Param("movieId") Long movieId);

}
