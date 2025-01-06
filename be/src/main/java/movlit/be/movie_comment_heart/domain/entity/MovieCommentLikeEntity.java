package movlit.be.movie_comment_heart.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MovieCommentLikeEntity {

    @EmbeddedId
    private MovieCommentLikeId movieCommentLikeId;

    @AttributeOverride(name = "value", column = @Column(name = "movie_comment_id"))
    private MovieCommentId movieCommentId;

    @AttributeOverride(name = "value", column = @Column(name = "member_id"))
    private MemberId memberId;

    private boolean isLiked;

    @Builder
    public MovieCommentLikeEntity(MovieCommentLikeId movieCommentLikeId, MovieCommentId movieCommentId,
                                  MemberId memberId, boolean isLiked) {
        this.movieCommentLikeId = movieCommentLikeId;
        this.movieCommentId = movieCommentId;
        this.memberId = memberId;
        this.isLiked = isLiked;
    }

}
