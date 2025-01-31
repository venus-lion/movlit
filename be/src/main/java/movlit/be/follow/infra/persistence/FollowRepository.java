package movlit.be.follow.infra.persistence;

import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.domain.Follow;

public interface FollowRepository {
    boolean existsByFollowerIdAndFolloweeId(MemberId followerId, MemberId followeeId);

    Follow save(Follow follow);

    Follow findByFollowerIdAndFolloweeId(MemberId followerId, MemberId followeeId);

    void delete(Follow follow);
}
