package movlit.be.movie.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MovieGenreEntity {

    @EmbeddedId
    private MovieGenreIdForEntity movieGenreIdForEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("movieId")
    @JoinColumn(name = "movie_id", referencedColumnName = "id", updatable = false, insertable = false)
    private MovieEntity movieEntity;
}
