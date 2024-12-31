package movlit.be.common.annotation;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.ids.MemberId;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class CurrentMemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentMemberId.class);
    }

    @Override
    public MemberId resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String id = (String) webRequest.getAttribute("id", RequestAttributes.SCOPE_REQUEST);
        log.info("ID in CurrentMemberIdArgumentResolver: {}", id); // 로그 추가
        return Objects.nonNull(id) ? new MemberId(id) : null;
    }

}
