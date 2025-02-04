package movlit.be.image.domain.repository;

import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.presentation.dto.response.ImageResponse;

public interface ImageRepository {

    ImageEntity upload(ImageEntity imageEntity);

    boolean existsByMemberId(MemberId memberId);

    ImageResponse fetchProfileImageByMemberId(MemberId memberId);

    void deleteByMemberId(MemberId memberId);

}
