package movlit.be.image.domain.repository;

import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.presentation.dto.response.ImageResponse;

public interface ImageRepository {

    ImageEntity upload(ImageEntity imageEntity);

    boolean existsByMemberIdInImage(MemberId memberId, ImageId imageId);

    ImageResponse fetchProfileImageByMemberId(MemberId memberId);

}
