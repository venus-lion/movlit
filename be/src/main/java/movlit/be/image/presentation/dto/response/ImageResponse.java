package movlit.be.image.presentation.dto.response;

import movlit.be.common.util.ids.ImageId;

public record ImageResponse(ImageId imageId, String url) {

}
