package movlit.be.movie.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Table(name = "movie_r_crew")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class MovieRCrewEntity {

    @EmbeddedId
    private MovieRCrewIdForEntity movieRCrewIdForEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieCrewId")
    @JoinColumn(name = "movie_crew_id")
    private MovieCrewEntity movieCrewEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private MovieEntity movieEntity;

}
