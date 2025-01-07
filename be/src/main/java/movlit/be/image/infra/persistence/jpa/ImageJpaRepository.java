package movlit.be.image.infra.persistence.jpa;

import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import movlit.be.image.presentation.dto.response.ImageResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, ImageId> {

    boolean existsByMemberId(MemberId memberId);

    @Query("SELECT NEW movlit.be.image.presentation.dto.response.ImageResponse(i.imageId, i.url) "
            + "FROM ImageEntity i "
            + "WHERE i.memberId = :memberId")
    ImageResponse findProfileImageByMemberId(@Param("memberId") MemberId memberId);

    void deleteByMemberId(MemberId memberId);

}
