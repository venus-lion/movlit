package movlit.be.image.application.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.application.convertor.ImageConvertor;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.domain.repository.ImageRepository;
import movlit.be.image.presentation.dto.response.ImageResponse;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;

    @Value("${aws.s3.bucket.folderName}")
    private String folderName;

    public ImageResponse uploadProfileImage(Member member, MultipartFile file) {
        MemberId memberId = member.getMemberId();
        ImageEntity imageEntity = ImageConvertor.toImageEntity(s3Service.uploadImage(file, folderName), memberId);
        validateMemberExistsInImage(memberId);
        ImageEntity savedImageEntity = imageRepository.upload(imageEntity);
        // 멤버 정보 update
        member.setProfileImgUrl(savedImageEntity.getUrl());
        memberRepository.save(member);
        return new ImageResponse(savedImageEntity.getImageId(), savedImageEntity.getUrl());
    }

    private void validateMemberExistsInImage(MemberId memberId) {
        if (imageRepository.existsByMemberId(memberId)) {
            imageRepository.deleteByMemberId(memberId);
        }
    }

    public ImageResponse fetchProfileImage(MemberId memberId) {
        return imageRepository.fetchProfileImageByMemberId(memberId);
    }

}
