package movlit.be.image.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.domain.repository.ImageRepository;
import movlit.be.image.infra.persistence.jpa.ImageJpaRepository;
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
    public boolean existsByMemberIdInImage(MemberId memberId, ImageId imageId) {
        return imageJpaRepository.existsByMemberIdInImage(memberId, imageId);
    }

}
