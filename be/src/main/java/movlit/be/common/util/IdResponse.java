package movlit.be.common.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BaseId;

@Getter
@Setter
@NoArgsConstructor
public class IdResponse {

    private BaseId id;

    private IdResponse(BaseId id) {
        this.id = id;
    }

    public static IdResponse of(BaseId id) {
        return new IdResponse(id);
    }

}
