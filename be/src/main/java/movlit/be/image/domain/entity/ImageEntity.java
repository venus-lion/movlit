package movlit.be.image.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.ImageId;
import movlit.be.common.util.ids.MemberId;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "image")
public class ImageEntity {

    @EmbeddedId
    private ImageId imageId;

    private String url;

    @AttributeOverride(name = "value", column = @Column(name = "member_id"))
    private MemberId memberId;

    private LocalDateTime regDt;

    public ImageEntity(ImageId imageId, String url, MemberId memberId) {
        this.imageId = imageId;
        this.url = url;
        this.memberId = memberId;
        this.regDt = LocalDateTime.now();
    }

}
