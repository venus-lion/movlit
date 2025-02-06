package movlit.be.auth.domain.repository;

public interface AuthCodeStorage {

    void saveCode(String code, String email);

    String fetchEmailForCode(String code);

    void removeCode(String code);

}
