package movlit.be.follow.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.follow.application.service.FollowWriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
@Slf4j
public class FollowController {
    private final FollowWriteService followWriteService;

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
        followWriteService.memberFollow(followerId, followeeId);

        return ResponseEntity.status(HttpStatus.OK).body("팔로우가 완료되었습니다.");
    }

    // 언팔로우 기능
    @DeleteMapping("/{followeeId}/follow")
    public ResponseEntity<String> memberUnFollow(
            @AuthenticationPrincipal MyMemberDetails details, // 현재 로그인한 사용자 정보
            @PathVariable MemberId followeeId // 언팔로우 대상 사용자 ID
    ){
        MemberId followerId = null;
        if (details != null){
            followerId = details.getMemberId();
        }
        followWriteService.memberUnFollow(followerId, followeeId);

        return ResponseEntity.status(HttpStatus.OK).body("언팔로우가 완료되었습니다.");
    }
}
