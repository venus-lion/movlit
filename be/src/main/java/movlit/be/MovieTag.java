package movlit.be;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MovieTag {

    @Id
    private MovieTagId movieTagId;

    @MapsId("movieId") // MovieTagId의 필드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="movie_id", updatable = false, insertable = false)
    private Movie movie;

    private String name;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;

    @Builder
    public MovieTag(MovieTagId movieTagId, Movie movie, String name, LocalDateTime regDt, LocalDateTime updDt,
                    boolean delYn) {
        this.movieTagId = movieTagId;
        this.movie = movie;
        this.name = name;
        this.regDt = regDt;
        this.updDt = updDt;
        this.delYn = delYn;
    }

}
