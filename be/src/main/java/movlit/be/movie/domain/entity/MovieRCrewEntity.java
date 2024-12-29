package movlit.be.movie.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Table(name = "movie_r_crew")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MovieRCrewEntity {

    @EmbeddedId
    private MovieRCrewIdREntity movieRCrewIdREntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_crew_id", referencedColumnName = "id", updatable = false, insertable = false)
    private MovieCrewEntity movieCrewEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", referencedColumnName = "id", updatable = false, insertable = false)
    private MovieEntity movieEntity;

}
