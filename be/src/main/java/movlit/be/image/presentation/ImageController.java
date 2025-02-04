package movlit.be.image.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.application.service.ImageService;
import movlit.be.image.presentation.dto.response.ImageResponse;
import movlit.be.member.application.service.MemberReadService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;
    private final MemberReadService memberReadService;

    @PostMapping("/api/images/profile")
    public ResponseEntity<ImageResponse> uploadProfileImage(@AuthenticationPrincipal MyMemberDetails details,
                                                            @RequestPart(value = "file", required = false) MultipartFile file) {
        MemberId memberId = details.getMemberId();
//        Member member = memberReadService.findByMemberId(memberId);
        var response = imageService.uploadProfileImage(memberId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/images/profile")
    public ResponseEntity<ImageResponse> fetchProfileImage(@AuthenticationPrincipal MyMemberDetails details) {
        var response = imageService.fetchProfileImage(details.getMemberId());
        return ResponseEntity.ok(response);
    }

}
