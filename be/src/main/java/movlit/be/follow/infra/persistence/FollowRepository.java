package movlit.be.follow.infra.persistence;

import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.domain.Follow;

public interface FollowRepository {

    boolean existsByFollowerIdAndFolloweeId(MemberId followerId, MemberId followeeId);

    boolean existsByFollowerIdAndFolloweeIdWithoutException(MemberId followerId, MemberId followeeId);

    Follow save(Follow follow);

    Follow findByFollowerIdAndFolloweeId(MemberId followerId, MemberId followeeId);

    void delete(Follow follow);

    List<Follow> findAllByFollowee_id(MemberId loginId);

    List<Follow> findAllByFollower_id(MemberId loginId);

    long countFollowersByLoginId(MemberId loginId);

    long countFollowsByLoginId(MemberId loginId);

}
