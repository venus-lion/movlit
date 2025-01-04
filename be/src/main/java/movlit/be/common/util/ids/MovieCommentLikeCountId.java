package movlit.be.common.util.ids;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode(callSuper = true)
public class MovieCommentLikeCountId extends BaseId {

    public MovieCommentLikeCountId(String value) {
        super(value);
    }

}

