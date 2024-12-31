//package movlit.be.common.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class JwtTokenUtil {
//
//    @Value("${jwt.secret}")
//    private String secret;
//    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10시간
//    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 14; // 2주
//
//    // 수정: Claims 추출 로직 변경
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    // 토큰에서 사용자 이름 추출
//    public String extractEmail(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    // 토큰에서 만료시간 추출
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public long extractExpirationAsLong(String token) {
//        return extractExpiration(token).getTime();
//    }
//
//    public boolean isTokenExpiredLong(String token) {
//        return extractExpirationAsLong(token) < System.currentTimeMillis();
//    }
//
//    // Access Token 생성 로직
//    public String createAccessToken(Map<String, Object> claims, String email) {
//        return createToken(claims, email, ACCESS_TOKEN_EXPIRATION_TIME);
//    }
//
//    // Refresh Token 생성 로직
//    public String createRefreshToken(Map<String, Object> claims, String email) {
//        return createToken(claims, email, REFRESH_TOKEN_EXPIRATION_TIME);
//    }
//
//    // Access Token 생성 로직
//    private String createToken(Map<String, Object> claims, String email, long time) {
//        // Secret Key를 UTF-8 인코딩의 바이트 배열로 변환
//        byte[] secretBytes = secret.getBytes(StandardCharsets.UTF_8);
//        Key key = Keys.hmacShaKeyFor(secretBytes);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + time))
//                .signWith(key, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    // 토큰 생성
//    public String generateAccessToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        return createAccessToken(claims, email);
//    }
//
//    public String generateRefreshToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        return createRefreshToken(claims, email);
//    }
//
//    // 토큰 유효성 검사 (만료 여부 확인 로직 추가)
//    public Boolean validateToken(String token, String email) {
//        final String extractedEmail = extractEmail(token);
//        return extractedEmail.equals(email) && !isTokenExpired(token);
//    }
//
//}