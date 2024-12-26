package movlit.be.ex.feed_collection_comment.domain.repository;

import com.foodymoody.be.common.util.ids.FeedCollectionCommentId;
import com.foodymoody.be.common.util.ids.MemberId;
import java.util.List;
import java.util.Optional;
import movlit.be.ex.feed_collection_comment.domain.FeedCollectionCommentSummary;
import movlit.be.ex.feed_collection_comment.domain.entity.FeedCollectionCommentEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FeedCollectionCommentRepository {

    FeedCollectionCommentEntity save(FeedCollectionCommentEntity feedCollectionComment);

    Optional<FeedCollectionCommentEntity> findById(FeedCollectionCommentId id);

    boolean existsById(FeedCollectionCommentId commentId);

    Slice<FeedCollectionCommentSummary> findSummaryAllByIdIn(
            MemberId memberId,
            List<FeedCollectionCommentId> commentIds,
            Pageable pageable
    );

    Slice<FeedCollectionCommentSummary> findSummaryAllByIdIn(
            List<FeedCollectionCommentId> commentIds,
            Pageable pageable
    );

}
