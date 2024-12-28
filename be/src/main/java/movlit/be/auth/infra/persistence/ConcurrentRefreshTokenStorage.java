package movlit.be.auth.infra.persistence;

import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.domain.repository.RefreshTokenStorage;
import movlit.be.common.util.ids.MemberId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcurrentRefreshTokenStorage implements RefreshTokenStorage {

    private final ConcurrentHashMap<String, String> refreshTokens = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    @Override
    public void saveRefreshToken(String email, String refreshToken) {
        refreshTokens.put(email, refreshToken);
    }

    @Override
    public String findByToken(String email) {
        return refreshTokens.get(email);
    }

    @Override
    public void addBlacklist(String token, long exp) {
        blacklist.put(token, exp);
    }

    @Override
    public boolean isBlacklist(String token) {
        return blacklist.containsKey(token);
    }

    @Override
    public void deleteByToken(String email) {
        refreshTokens.remove(email);
    }

}
