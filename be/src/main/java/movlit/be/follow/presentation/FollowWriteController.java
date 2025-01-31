package movlit.be.follow.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.application.service.FollowService;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
@Slf4j
public class FollowWriteController {
    private final FollowService followService;
    private final MemberReadService memberReadService;

    // 팔로우 기능
    // 로그인한 유저(@AuthenticationPrincipal)가, URL에 있던 memberId를 팔로우한다.
    @PostMapping("/{followeeId}/follow")
    public ResponseEntity<String> memberFollow(
            @AuthenticationPrincipal MyMemberDetails details, // 현재 로그인한 사용자 정보
            @PathVariable MemberId followeeId // 팔로우할 대상 사용자 ID

    ){
        MemberId followerId = null;
        if(details != null) {
            followerId = details.getMemberId();
        }

        Member follower = memberReadService.findByMemberId(followerId);
        Member followee = memberReadService.findByMemberId(followeeId);
        log.info("팔로우한 사람 >> {}", follower);
        log.info("팔로우된 사람, 찾았나요?? >> {}", followee);

        followService.memberFollow(followerId, followeeId);

        return ResponseEntity.status(HttpStatus.OK).body("팔로우가 완료되었습니다.");
    }
}
