package movlit.be.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${share.url}")
    private String url; // 배포 환경의 프론트엔드 URL

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해
                .allowedOrigins(url) //  허용할 출처
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
                .allowedHeaders("*")
//                .allowedHeaders("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin") // 허용할 헤더 추가
                .allowCredentials(true); // 쿠키 허용
    }
}