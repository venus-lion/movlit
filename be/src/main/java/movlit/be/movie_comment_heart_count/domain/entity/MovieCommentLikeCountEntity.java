package movlit.be.movie_comment_heart_count.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieHeartCountId;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class MovieCommentLikeCountEntity {

    @EmbeddedId
    private MovieHeartCountId movieHeartCountId;

    @AttributeOverride(name = "value", column = @Column(name = "movie_comment_id"))
    private MovieCommentId movieCommentId;

    private Long count;

    @Version
    private Long version;

    @Builder
    public MovieCommentLikeCountEntity(MovieHeartCountId movieHeartCountId, MovieCommentId movieCommentId, Long count,
                                       Long version) {
        this.movieHeartCountId = movieHeartCountId;
        this.movieCommentId = movieCommentId;
        this.count = count;
        this.version = version;
    }

}
