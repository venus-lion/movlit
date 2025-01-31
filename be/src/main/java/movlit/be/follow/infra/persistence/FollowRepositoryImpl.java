package movlit.be.follow.infra.persistence;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.FollowAlreadyMemberException;
import movlit.be.common.exception.FollowNotFoundException;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.domain.Follow;
import movlit.be.follow.infra.persistence.jpa.FollowJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepository {
    private final FollowJpaRepository followJpaRepository;

    // 이미 팔로우한 경우 체크
    @Override
    public boolean existsByFollowerIdAndFolloweeId(MemberId followerId, MemberId followeeId) {
        if (followJpaRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)){
            throw new FollowAlreadyMemberException();
        }
        return false;
    }

    @Override
    public Follow save(Follow follow) {
        return followJpaRepository.save(follow);
    }

    @Override
    public Follow findByFollowerIdAndFolloweeId(MemberId followerId, MemberId followeeId) {
        Follow follow = followJpaRepository.findByFollowerIdAndFolloweeId(followerId, followeeId)
                .orElseThrow(() -> new FollowNotFoundException());

        return follow;
    }

    @Override
    public void delete(Follow follow) {
        followJpaRepository.delete(follow);
    }

    // '나를 팔로우하는 사람들 목록'을 조회하는 쿼리
    @Override
    public List<Follow> findAllByFollowee_id(MemberId loginId) {
        return followJpaRepository.findAllByFollowee_Id(loginId);
    }

}
