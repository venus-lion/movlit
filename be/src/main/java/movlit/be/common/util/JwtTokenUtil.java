package movlit.be.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.RequiredTypeException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import movlit.be.common.exception.ClaimNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10시간
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14; // 2주

    // 수정: Claims 추출 로직 변경
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 사용자 이름 추출
    public Map<String, String> parseAccessToken(String token) {
        Claims claims = extractAllClaims(token);
        String id = getClaim(claims, "id", String.class);
        String email = getClaim(claims, "email", String.class);
        return Map.of("id", id, "email", email);
    }

    // 토큰에서 만료시간 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public long extractExpirationAsLong(String token) {
        return extractExpiration(token).getTime();
    }

    public boolean isTokenExpiredLong(String token) {
        return extractExpirationAsLong(token) < System.currentTimeMillis();
    }

    // Access Token 생성 로직
    public String createAccessToken(Map<String, Object> idClaims, Map<String, Object> emailClaims) {
        return createToken(idClaims, emailClaims, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    // Refresh Token 생성 로직
    public String createRefreshToken(Map<String, Object> idClaims, Map<String, Object> emailClaims) {
        return createToken(idClaims, emailClaims, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    // 토큰 생성 (Access Token / Refresh Token 통합)
    private String createToken(Map<String, Object> idClaims, Map<String, Object> emailClaims, long time) {
        // Secret Key를 UTF-8 인코딩의 바이트 배열로 변환
        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        Key key = Keys.hmacShaKeyFor(secretBytes);

        // ID와 이메일을 별도의 클레임으로 설정
        Map<String, Object> claims = new HashMap<>();
        claims.putAll(idClaims);
        claims.putAll(emailClaims);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(emailClaims.get("email").toString()) // Subject를 이메일로 설정
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰 생성
    public String generateAccessToken(String id, String email) {
        Map<String, Object> idClaims = Map.of("id", id);
        Map<String, Object> emailClaims = Map.of("email", email);
        return createAccessToken(idClaims, emailClaims);
    }

    public String generateRefreshToken(String id, String email) {
        Map<String, Object> idClaims = Map.of("id", id);
        Map<String, Object> emailClaims = Map.of("email", email);
        return createRefreshToken(idClaims, emailClaims);
    }

    // 토큰 유효성 검사 (만료 여부 확인 로직 추가)
    public Boolean validateToken(String token, String id) {
        final String extractedId = parseAccessToken(token).get("id");
        return extractedId.equals(id) && !isTokenExpired(token);
    }

    private <T> T getClaim(Claims claims, String key, Class<T> type) {
        try {
            T claim = claims.get(key, type);
            if (Objects.isNull(claim)) {
                throw new ClaimNotFoundException();
            }
            return claim;
        } catch (RequiredTypeException | ClassCastException e) {
            throw new IncorrectClaimException(null, claims, key);
        }
    }

}