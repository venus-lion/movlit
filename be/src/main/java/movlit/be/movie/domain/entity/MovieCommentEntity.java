package movlit.be.movie.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;

@Table(name = "movie_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MovieCommentEntity {

    @EmbeddedId
    private MovieCommentId movieCommentId;

    @AttributeOverride(name = "value", column = @Column(name = "member_id"))
    private MemberId memberId; // Member 하나에 Comment 하나

    private Long movieId; // Movie 하나에 Comment 하나

    private String comment;
    private Integer score;
    private LocalDateTime regDt;
    private LocalDateTime updDt;
    private boolean delYn;

    @Builder
    public MovieCommentEntity(MovieCommentId movieCommentId, MemberId memberId, Long movieId, String comment,
                              Integer score,
                              LocalDateTime regDt, LocalDateTime updDt, boolean delYn) {
        this.movieCommentId = movieCommentId;
        this.memberId = memberId;
        this.movieId = movieId;
        this.comment = comment;
        this.score = score;
        this.regDt = regDt;
        this.updDt = updDt;
        this.delYn = delYn;
    }

}
