package movlit.be.follow.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
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

    @Query("SELECT f FROM Follow f " +
            "WHERE f.follower.memberId = :followerId AND f.followee.memberId = :followeeId")
    Optional<Follow> findByFollowerIdAndFolloweeId(@Param("followerId")MemberId followerId,
                                                   @Param("followeeId")MemberId followeeId);


    // 나를 팔로우하는 사람들(내 팔로워) 조회 - fetch join으로 내 팔로워 정보까지 한번에 가져오기 (성능 개선)
    @Query("SELECT f FROM Follow f " +
            "JOIN FETCH f.follower " +
            "WHERE f.followee.memberId = :loginId")
    List<Follow> findAllByFollowee_Id(MemberId loginId);

    // 내가 팔로우하는 사람들(내 팔로우) 조회
    @Query("SELECT f FROM Follow f " +
            "JOIN FETCH f.followee " +
            "WHERE f.follower.memberId = :loginId")
    List<Follow> findAllByFollower_id(MemberId loginId);

    // 나를 팔로우하는 사람들(내 팔로워) 개수 조회
    @Query("SELECT COUNT(f) FROM Follow f " +
            "WHERE f.followee.memberId = :loginId")
    long countFollowersByLoginId(MemberId loginId);

    // 내가 팔로우하는 사람들(내 팔로우) 개수 조회
    @Query("SELECT COUNT(f) FROM Follow f " +
            "WHERE f.follower.memberId = :loginId")
    long countFollowsByLoginId(MemberId loginId);
}
