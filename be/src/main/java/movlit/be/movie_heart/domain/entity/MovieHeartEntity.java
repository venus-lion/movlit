package movlit.be.movie_heart.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieHeartId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MovieHeartEntity {

    @EmbeddedId
    private MovieHeartId movieHeartId;

    private Long movieId;

    @AttributeOverride(name = "value", column = @Column(name = "member_id"))
    private MemberId memberId;

    private boolean isHearted;

    @Builder
    public MovieHeartEntity(MovieHeartId movieHeartId, Long movieId, MemberId memberId, boolean isHearted) {
        this.movieHeartId = movieHeartId;
        this.movieId = movieId;
        this.memberId = memberId;
        this.isHearted = isHearted;
    }

}
