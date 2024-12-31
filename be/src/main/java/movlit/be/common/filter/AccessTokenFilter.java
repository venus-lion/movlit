package movlit.be.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.ErrorMessage;
import movlit.be.common.exception.ErrorResponse;
import movlit.be.common.exception.UnauthorizedException;
import movlit.be.common.util.HttpHeaderParser;
import movlit.be.common.util.HttpHeaderType;
import movlit.be.new_auth.application.service.TokenService;
import movlit.be.new_auth.infra.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class AccessTokenFilter implements Filter {

    @Value("${filter.whitelist}")
    private List<String> whiteList;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = getMethod(httpRequest);
        String uri = getUri(httpRequest);

        try {
            String header = httpRequest.getHeader(HttpHeaderType.AUTH.headerName);
            if (Objects.nonNull(header)) {
                String token = HttpHeaderParser.parse(header, HttpHeaderType.AUTH);
                tokenService.validateNotBlacklisted(token);
                Map<String, String> parsed = jwtUtil.parseAccessToken(token);
                request.setAttribute("id", parsed.get("id"));
                request.setAttribute("email", parsed.get("email"));
                chain.doFilter(request, response);
                return;
            }

            if (!isInWhiteList(method, uri)) {
                throw new UnauthorizedException();
            }
        } catch (JwtException e) {
            sendError(response, e, ErrorMessage.INVALID_TOKEN.getCode());
            return;
        } catch (Exception e) {
            sendError(response, e, ErrorMessage.UNAUTHORIZED.getCode());
            return;
        }

        chain.doFilter(request, response);
    }

    private void sendError(ServletResponse response, Exception e, String code) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter writer = httpResponse.getWriter();
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), code);
        String jsonErrorResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);
        writer.write(jsonErrorResponse);
        writer.flush();
    }

    private String getMethod(HttpServletRequest httpRequest) {
        return httpRequest.getMethod();
    }

    private String getUri(HttpServletRequest httpRequest) {
        return httpRequest.getRequestURI();
    }

    private boolean isInWhiteList(String method, String uri) {
        return whiteList.stream()
                .map(pattern -> pattern.split(" ", 2))
                .filter(parts -> parts.length == 2)
                .filter(parts -> parts[0].equals(method))
                .anyMatch(parts -> {
                    String regex = parts[1]
                            .replace(".", "\\.")
                            .replace("*", ".*");
                    return Pattern.matches(regex, uri);
                });
    }

}