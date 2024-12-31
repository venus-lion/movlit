package movlit.be.movie.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MovieGenreIdForEntity implements Serializable {

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "genre_id")
    private Long genreId;

}
