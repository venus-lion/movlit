package movlit.be.follow.infra.persistence.jpa;

import movlit.be.common.util.ids.FollowId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowJpaRepository extends JpaRepository<Follow, FollowId> {
    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END " +
            "FROM Follow f " +
            "WHERE f.follower.memberId = :followerId AND f.followee.memberId = :followeeId")
    boolean existsByFollowerIdAndFolloweeId(@Param("followerId")MemberId followerId,
                                            @Param("followeeId")MemberId followeeId);
}
