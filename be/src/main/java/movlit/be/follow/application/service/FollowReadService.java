package movlit.be.follow.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.domain.Follow;
import movlit.be.follow.infra.persistence.FollowRepository;
import movlit.be.follow.presentation.dto.FollowResponse;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.entity.MemberEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowReadService {
    private final FollowRepository followRepository;
    private final MemberReadService memberReadService;

    @Transactional(readOnly = true)
    public List<FollowResponse> getMyFollowersDetails(
            MemberId loginId
    ){
        // 해당 loginId에 해당하는 Member가 존재하지 않는다면, 예외 발생
        MemberEntity loginMember = memberReadService.findEntityById(loginId);
        log.info("FollwerReadService >>> loginMember : {}", loginMember);

        // 나를 팔로우하는(팔로워) 사람들 조회
        List<Follow> followerList = followRepository.findAllByFollowee_id(loginId);

        List<FollowResponse> followResponseList = followerList.stream()
                .map(this::toFollowResponse)
                .toList();

        for (FollowResponse followResponse : followResponseList){
            log.info("FollowReadService >> followResponse : {}", followResponse);
        }
        return followResponseList;

    }

    @Transactional(readOnly = true)
    public List<FollowResponse> getMyFollowingDetail(
            MemberId loginId
    ){
        // 해당 loginId에 해당하는 Member가 존재하지 않는다면, 예외 발생
        MemberEntity loginMember = memberReadService.findEntityById(loginId);
        log.info("FollwerReadService >>> loginMember : {}", loginMember);

        // 내가 팔로우하는(팔로잉) 사람들 조회
        List<Follow> followingList = followRepository.findAllByFollower_id(loginId);

        return followingList.stream()
                .map(this::toFollowingResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public int getFollowerCount(
            MemberId loginId
    ){
        // 해당 loginId에 해당하는 Member가 존재하지 않는다면, 예외 발생
        MemberEntity loginMember = memberReadService.findEntityById(loginId);
        log.info("FollwerReadService >>> loginMember : {}", loginMember);

        // 나를 팔로우하는 사람들(팔로워) 개수 조회
        int followerCount = (int) followRepository.countFollowersByLoginId(loginId);
        log.info("followerCount 개수 >>> {}", followerCount);
        return followerCount;
    }

    @Transactional(readOnly = true)
    public int getFollowCount(
            MemberId loginId
    ){
        // 해당 loginId에 해당하는 Member가 존재하지 않는다면, 예외 발생
        memberReadService.findEntityById(loginId);

        // 내가 팔로우하는 사람들(팔로우) 개수 조회
        return (int) followRepository.countFollowsByLoginId(loginId);
    }

    @Transactional(readOnly = true)
    public boolean isFollowing(MemberId loginId, MemberId otherMemberId){
        return followRepository.existsByFollowerIdAndFolloweeIdWithoutException(loginId, otherMemberId);
    }
    private FollowResponse toFollowResponse(Follow follow){
        MemberEntity follower = follow.getFollower(); // 나를 팔로우하는 사람(팔로워) 정보 가져오기

        return FollowResponse.builder()
                .memberId(follower.getMemberId())
                .nickname(follower.getNickname())
                .profileImgUrl(follower.getProfileImgUrl())
                .build();
    }

    private FollowResponse toFollowingResponse(Follow follow){
        MemberEntity followee = follow.getFollowee(); // 내가 팔로우하는 사람 정보 가져오기

        return FollowResponse.builder()
                .memberId(followee.getMemberId())
                .nickname(followee.getNickname())
                .profileImgUrl(followee.getProfileImgUrl())
                .build();
    }

}
