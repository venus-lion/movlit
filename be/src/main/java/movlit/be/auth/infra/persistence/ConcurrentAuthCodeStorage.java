package movlit.be.auth.infra.persistence;

import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.domain.repository.AuthCodeStorage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcurrentAuthCodeStorage implements AuthCodeStorage {

    private final ConcurrentHashMap<String, String> codeMap = new ConcurrentHashMap<>();

    @Override
    public void saveCode(String code, String email) {
        codeMap.put(code, email);
    }

    @Override
    public String fetchEmailForCode(String code) {
        return codeMap.get(code);
    }

    @Override
    public void removeCode(String code) {
        codeMap.remove(code);
    }

}
