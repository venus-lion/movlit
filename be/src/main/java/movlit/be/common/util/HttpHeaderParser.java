package movlit.be.common.util;

import java.util.Objects;
import movlit.be.common.exception.AuthHeaderNotFoundException;

public class HttpHeaderParser {

    private HttpHeaderParser() {
        // TODO: 정하기
        throw new IllegalStateException();
    }

    public static String parse(String authHeader, HttpHeaderType headerType) {
        if (Objects.isNull(authHeader)) {
            throw new AuthHeaderNotFoundException();
        }

        return authHeader.substring(headerType.skim.length() + 1);
    }

}
