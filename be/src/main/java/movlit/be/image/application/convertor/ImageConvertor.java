package movlit.be.image.application.convertor;

import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.image.domain.entity.ImageEntity;

public class ImageConvertor {

    public static ImageEntity toImageEntity(String url, MemberId memberId) {
        return new ImageEntity(IdFactory.createImageId(), url, memberId);
    }

}
