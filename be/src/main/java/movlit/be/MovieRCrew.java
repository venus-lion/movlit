package movlit.be;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class MovieRCrew {

    @EmbeddedId
    private MovieRCrewId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_crew_id", updatable = false, insertable = false)
    private MovieCrew movieCrew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", updatable = false, insertable = false)
    private Movie movie;

}
