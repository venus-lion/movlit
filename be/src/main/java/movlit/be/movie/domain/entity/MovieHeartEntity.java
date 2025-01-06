package movlit.be.movie.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name="movie_heart")
public class MovieHeartEntity {

    @EmbeddedId
    private MovieHeartId movieHeartId;

    private Long movieId;

    @AttributeOverride(name = "value", column = @Column(name = "member_id"))
    private MemberId memberId;

    private boolean isHearted;

    private LocalDateTime regDt;

    @Builder
    public MovieHeartEntity(MovieHeartId movieHeartId, Long movieId, MemberId memberId, boolean isHearted) {
        this.movieHeartId = movieHeartId;
        this.movieId = movieId;
        this.memberId = memberId;
        this.isHearted = isHearted;
    }

}