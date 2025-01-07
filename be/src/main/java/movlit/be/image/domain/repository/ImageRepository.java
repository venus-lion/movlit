package movlit.be.image.domain.repository;

import movlit.be.image.domain.entity.ImageEntity;

public interface ImageRepository {

    ImageEntity upload(ImageEntity imageEntity);

}
