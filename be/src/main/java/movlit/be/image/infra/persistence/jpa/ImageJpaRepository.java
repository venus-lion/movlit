package movlit.be.image.infra.persistence.jpa;

import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, ImageId> {

    @Query("SELECT "
            + "CASE WHEN COUNT(i) > 0 THEN true ELSE false END "
            + "FROM ImageEntity i "
            + "WHERE i.memberId = :memberId AND i.imageId = :imageId")
    boolean existsByMemberIdInImage(@Param("memberId") MemberId memberId, @Param("imageId") ImageId imageId);

}
