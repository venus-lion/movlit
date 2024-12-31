package movlit.be.common.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "*");
        httpResponse.setHeader("Access-Control-Max-Age", "86400");

        if (isPreFlightRequest(httpRequest)) {
            httpResponse.setStatus(HttpStatus.ACCEPTED.value());
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPreFlightRequest(HttpServletRequest request) {
        return (HttpMethod.OPTIONS.matches(request.getMethod()) &&
                request.getHeader(HttpHeaders.ORIGIN) != null &&
                request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD) != null);
    }

}
