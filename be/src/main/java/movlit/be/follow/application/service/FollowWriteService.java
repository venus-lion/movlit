package movlit.be.follow.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.FollowSelfNotAllowedException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.domain.Follow;
import movlit.be.follow.infra.persistence.FollowRepository;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowWriteService {
    private final FollowRepository followRepository;
    private final MemberReadService memberReadService;

    @Transactional
    public Follow memberFollow(
            MemberId followerId,
            MemberId followeeId
    ){
        if (followerId.equals(followeeId)){
            throw new FollowSelfNotAllowedException();
        }

        // 이미 팔로우한 경우 예외처리
        followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);

        return register(followerId, followeeId);
    }

    @Transactional
    public void memberUnFollow(
            MemberId followerId,
            MemberId followeeId
    ){
        // 언팔로우를 시도했는데, 팔로우 관계가 존재하지 않는다면 예외 처리
        Follow follow = followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);

        followRepository.delete(follow);
    }

    public Follow register(
            MemberId followerId,
            MemberId followeeId
    ){
        MemberEntity follower = memberReadService.findEntityById(followerId);
        MemberEntity followee = memberReadService.findEntityById(followeeId);

        Follow follow = Follow.builder()
                .followId(IdFactory.createFollowId())
                .follower(follower)
                .followee(followee)
                .build();

        return followRepository.save(follow);
    }
}
