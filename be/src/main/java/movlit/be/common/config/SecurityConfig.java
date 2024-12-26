package movlit.be.common.config;

import movlit.be.auth.application.service.MyOAuth2MemberService;
import movlit.be.common.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private MyOAuth2MemberService myOAuth2MemberService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(auth -> auth.disable())       // CSRF 방어 기능 비활성화
                .headers(x -> x.frameOptions(y -> y.disable()))     // H2-console
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/book/list", "/book/detail/**", "/bookEs/list", "/bookEs/detail/**",
                                "/misc/**", "/actuator/**", "/restaurant/list", "/restaurant/detail/**",
                                "/websocket/**", "/echo", "/personal",
                                "/mall/list", "/mall/detail/**", "/member/register", "/h2-console", "/demo/**",
                                "/img/**", "/js/**", "/css/**", "/error/**").permitAll()
                        .requestMatchers("/book/insert", "/book/yes24", "/bookEs/yes24", "/order/listAll",
                                "/restaurant/init",
                                "/order/bookStat", "/member/delete", "/member/list").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(auth -> auth
                        .loginPage("/member/login")       // login form
                        .loginProcessingUrl("/member/login")      // 스프링이 낚아 챔. MemberDetailsService 구현 객체에서 처리해주어야 함
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/member/loginSuccess", true)  // 로그인 후 해야할 일
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(auth -> auth
                        .logoutUrl("/member/logout")
                        .invalidateHttpSession(true)        // 로그아웃시 세션 삭제
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/member/login")
                )
                .oauth2Login(auth -> auth
                        .loginPage("/member/login")
                        // 1. 코드받기(인증), 2. 액세스 토큰(권한), 3. 사용자 정보 획득
                        // 4. 3에서 받은 정보를 토대로 DB에 없으면 가입을 시켜줌
                        // 5. 프로바이더가 제공하는 정보
                        .userInfoEndpoint(
                                userInfoEndpointConfig -> userInfoEndpointConfig.userService(myOAuth2MemberService))
                        .defaultSuccessUrl("/member/loginSuccess", true)
                )
        ;

        return http.build();
    }

    // JWT Filter Bean 등록
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    // Authentication Manager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
