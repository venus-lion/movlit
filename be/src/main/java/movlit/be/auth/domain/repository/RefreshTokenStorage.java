package movlit.be.auth.domain.repository;

public interface RefreshTokenStorage {

    void saveRefreshToken(String email, String refreshToken);

    String findByToken(String email);

    void addBlacklist(String token, long exp);

    boolean isBlacklist(String token);

    void deleteByToken(String email);

}
