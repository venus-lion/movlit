package movlit.be.common.util.ids;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode(callSuper = true)
public class ImageId extends BaseId {

    public ImageId(String value) {
        super(value);
    }

}

