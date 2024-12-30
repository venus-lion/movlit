package movlit.be.movie.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class MovieGenreIdEntity implements Serializable {

    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "movie_id")
    private Long movieId;

}