package movlit.be.image.application.service;

import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.ImageAlreadyExistsInMemberException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.domain.repository.ImageRepository;
import movlit.be.image.presentation.dto.response.ImageResponse;
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

    private final S3Client s3Client;
    private final ImageRepository imageRepository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    @Value("${aws.s3.bucket.folderName}")
    private String folderName;

    public ImageResponse uploadProfileImage(MemberId memberId, MultipartFile file) {
        ImageId imageId = IdFactory.createImageId();
        String url = uploadImage(file, folderName);
        ImageEntity imageEntity = new ImageEntity(imageId, url, memberId);
        validateMemberExistsInImage(memberId, imageEntity.getImageId());
        ImageEntity savedImageEntity = imageRepository.upload(imageEntity);
        return new ImageResponse(savedImageEntity.getImageId(), savedImageEntity.getUrl());
    }

    private void validateMemberExistsInImage(MemberId memberId, ImageId imageId) {
        if (imageRepository.existsByMemberIdInImage(memberId, imageId)) {
            throw new ImageAlreadyExistsInMemberException();
        }
    }

    private String uploadImage(MultipartFile file, String folderName) {
        String fileName = generateFileName(file.getOriginalFilename(), folderName);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName)).toExternalForm();
    }

    private String generateFileName(String originalFilename, String folderName) {
        return folderName + "/" + UUID.randomUUID() + "-" + originalFilename;
    }

}
