package movlit.be.movie.infra.persistence.jpa;

import java.util.List;
import movlit.be.movie.domain.entity.MovieRCrewEntity;
import movlit.be.movie.domain.entity.MovieRCrewIdForEntity;
import movlit.be.movie.presentation.dto.response.MovieCrewResponseDto;
import movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRCrewJpaRepository extends JpaRepository<MovieRCrewEntity, MovieRCrewIdForEntity> {

    @Query("SELECT NEW movlit.be.movie.presentation.dto.response.MovieDetailCrewResponse(mc.movieCrewEntity.profileImgUrl, mc.movieCrewEntity.name, mc.movieCrewEntity.charName, mc.movieCrewEntity.role) "
            + "FROM MovieRCrewEntity mc "
            + "WHERE mc.movieRCrewIdForEntity.movieId = :movieId")
    List<MovieDetailCrewResponse> findMovieCrewsByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT new movlit.be.movie.presentation.dto.response.MovieCrewResponseDto( " +
            "mrc.movieRCrewIdForEntity.movieId, mrc.movieCrewEntity.movieCrewId, " +
            "mrc.movieCrewEntity.name, mrc.movieCrewEntity.role, mrc.movieCrewEntity.orderNo) " +
            "FROM MovieRCrewEntity mrc " +
            "WHERE mrc.movieRCrewIdForEntity.movieId IN :movieIds")
    List<MovieCrewResponseDto> findMovieCrewByMovieIds(@Param("movieIds") List<Long> movieIds);

}
