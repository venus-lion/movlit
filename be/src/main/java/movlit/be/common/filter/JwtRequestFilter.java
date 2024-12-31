package movlit.be.common.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.AuthTokenService;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.auth.application.service.MyMemberDetailsService;
import movlit.be.common.util.HttpHeaderParser;
import movlit.be.common.util.HttpHeaderType;
import movlit.be.common.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtil;
    private final MyMemberDetailsService myMemberDetailsService;
    private final AuthTokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = null;

        try {
            String header = request.getHeader(HttpHeaderType.AUTH.headerName);
            if (Objects.nonNull(header)) {
                token = HttpHeaderParser.parse(header, HttpHeaderType.AUTH);
                tokenService.validateNotBlacklisted(token);
                Map<String, String> parsed = jwtUtil.parseAccessToken(token);
                String id = parsed.get("id");
                String email = parsed.get("email");

                // 사용자 ID와 이메일을 request attribute에 저장
                request.setAttribute("id", id);
                request.setAttribute("email", email);

                log.info("ID in JwtRequestFilter: {}", id); // 로그 추가
                log.info("Email in JwtRequestFilter: {}", email); // 로그 추가

                // SecurityContext에 인증 정보 설정
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    MyMemberDetails memberDetails = this.myMemberDetailsService.loadUserByUsername(email);

                    if (jwtUtil.validateToken(token, memberDetails.getMember().getMemberId().getValue())) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(memberDetails, null,
                                        memberDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            }
        } catch (ExpiredJwtException e) {
            // 토큰 만료 처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token Expired");
            return; // 필터 체인 중단
        } catch (JwtException e) {
            // 기타 토큰 관련 에러 처리 (잘못된 토큰, 서명 오류 등)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return; // 필터 체인 중단
        } catch (Exception e) {
            // 기타 예외 처리 (NullPointerException 등)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
            return; // 필터 체인 중단
        }

        chain.doFilter(request, response);
    }
}