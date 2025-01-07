package movlit.be.image.domain.repository;

import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;

public interface ImageRepository {

    ImageEntity upload(ImageEntity imageEntity);

    boolean existsByMemberIdInImage(MemberId memberId, ImageId imageId);

}
