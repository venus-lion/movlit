package movlit.be.image.infra.persistence.jpa;

import movlit.be.common.util.ids.ImageId;
import movlit.be.image.domain.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageJpaRepository extends JpaRepository<ImageEntity, ImageId> {

}
