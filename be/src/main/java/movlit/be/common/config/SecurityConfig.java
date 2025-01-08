package movlit.be.common.config;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.MyOAuth2MemberService;
import movlit.be.auth.application.service.OAuth2AuthenticationSuccessHandler;
import movlit.be.common.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFailureHandler failureHandler;
    private final MyOAuth2MemberService myOAuth2MemberService;
    private final JwtRequestFilter jwtRequestFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)       // CSRF 방어 기능 비활성화
                .headers(x -> x.frameOptions(FrameOptionsConfig::disable))     // H2-console
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/testBook/**").permitAll()
                        .requestMatchers("/testBook//saveBooks/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/*/detail/related").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/images/profile").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/images/profile").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/movies/*/hearts").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/*/hearts").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/movies/comments/*/likes").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/comments/*/likes").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/movies/*/comments").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/movies/*/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/movies/*/comments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/members/myPage").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/members/genreList").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/movies/search/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/genreList").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/*/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/{movieId}/myComment").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/movies/*/crews").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/*/genres").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/*/detail").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/members/register").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/members/update").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/members/delete").authenticated()
                        .requestMatchers("/api/movies/main/**", "/collect/indices/**", "/collect/movie/**", "/discover",
                                "/websocket/**", "/echo", "/api/members/login", "/img/**", "/js/**", "/css/**",
                                "/error/**", "api/books/**")
                        .permitAll()
                        .requestMatchers("/api/members/delete", "/api/members/list").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/api/members/logout")
                        .permitAll()
                        .logoutSuccessHandler(((request, response, authentication) -> response.setStatus(
                                HttpServletResponse.SC_NO_CONTENT)))
                        .deleteCookies("refreshToken")
                )
                .oauth2Login(auth -> auth
                        .loginPage("/member/login")
                        // 1. 코드받기(인증), 2. 액세스 토큰(권한), 3. 사용자 정보 획득
                        // 4. 3에서 받은 정보를 토대로 DB에 없으면 가입을 시켜줌
                        // 5. 프로바이더가 제공하는 정보
                        .userInfoEndpoint(
                                userInfoEndpointConfig -> userInfoEndpointConfig.userService(myOAuth2MemberService)
                        )
                        .successHandler(oAuth2AuthenticationSuccessHandler)
//                       .defaultSuccessUrl("http://localhost:5173", true)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        ;

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Vite-React 앱의 출처
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Authentication Manager 빈 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}