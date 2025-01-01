package movlit.be.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetailsService;
import movlit.be.common.util.JwtTokenUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final MyMemberDetailsService myMemberDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        log.info("JwtRequestFilter executed");

        // 1. Authorization 헤더 추출
        final String authorizationHeader = request.getHeader("Authorization");
        log.info("1. Authorization Header: {}", authorizationHeader); // 1. 헤더 값 확인

        String email = null;
        String jwt = null;

        // 2. Bearer 토큰 형식 검사
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.info("2. JWT Token: {}", jwt); // 2. JWT 토큰 확인

            try {
                email = jwtTokenUtil.extractEmail(jwt);
                log.info("3. Extracted Email: {}", email); // 3. Email 추출 확인
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token Expired");
                log.warn("Token Expired");
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Token");
                log.error("Invalid Token", e);
                return;
            }
        } else {
            log.warn("Authorization header does not start with Bearer or is null"); // Bearer 토큰 형식이 아닐 경우 로그 추가
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails memberDetails = this.myMemberDetailsService.loadUserByUsername(email);
            log.info("Loaded UserDetails: {}", memberDetails); // UserDetails 로드 확인

            try {
                boolean isValid = jwtTokenUtil.validateToken(jwt, memberDetails.getUsername());
                log.info("Token Validation Result: {}", isValid); // 토큰 검증 결과 확인

                if (isValid) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(memberDetails, null,
                                    memberDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    log.info("SecurityContext Authentication: {}",
                            SecurityContextHolder.getContext().getAuthentication()); // SecurityContext 저장 확인
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\": \"TOKEN_INVALID\", \"message\": \"Invalid Token\"}");
                    return;
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\": \"TOKEN_EXPIRED\", \"message\": \"Token Expired\"}");
                return;
            }
        }
        chain.doFilter(request, response);
        SecurityContextHolder.clearContext();
    }

}