package movlit.be.common.util.ids;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@EqualsAndHashCode(callSuper = true)
public class NotificationId extends BaseId {

    public NotificationId(String value) {
        super(value);
    }

}
