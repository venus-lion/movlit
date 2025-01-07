package movlit.be.image.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.domain.repository.ImageRepository;
import movlit.be.image.infra.persistence.jpa.ImageJpaRepository;
import movlit.be.image.presentation.dto.response.ImageResponse;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepository {

    private final ImageJpaRepository imageJpaRepository;

    @Override
    public ImageEntity upload(ImageEntity imageEntity) {
        return imageJpaRepository.save(imageEntity);
    }

    @Override
    public boolean existsByMemberId(MemberId memberId) {
        return imageJpaRepository.existsByMemberId(memberId);
    }

    @Override
    public ImageResponse fetchProfileImageByMemberId(MemberId memberId) {
        return imageJpaRepository.findProfileImageByMemberId(memberId);
    }

    @Override
    public void deleteByMemberId(MemberId memberId) {
        imageJpaRepository.deleteByMemberId(memberId);
    }

}
