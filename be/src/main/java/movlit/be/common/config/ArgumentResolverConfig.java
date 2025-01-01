package movlit.be.common.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.annotation.CurrentMemberIdArgumentResolver;
import movlit.be.common.annotation.MemberEmailArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ArgumentResolverConfig implements WebMvcConfigurer {

    private final MemberEmailArgumentResolver memberEmailArgumentResolver;
//    private final CurrentMemberIdArgumentResolver currentMemberIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberEmailArgumentResolver);
//        resolvers.add(currentMemberIdArgumentResolver);
    }

}
