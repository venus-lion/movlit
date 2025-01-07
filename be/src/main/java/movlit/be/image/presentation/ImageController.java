package movlit.be.image.presentation;

import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.image.application.service.ImageService;
import movlit.be.image.presentation.dto.response.ImageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/api/images/profile")
    public ResponseEntity<ImageResponse> uploadProfileImage(@AuthenticationPrincipal MyMemberDetails details,
                                                            @RequestPart MultipartFile file) {
        var response = imageService.uploadProfileImage(details.getMemberId(), file);
        return ResponseEntity.ok(response);
    }

}
