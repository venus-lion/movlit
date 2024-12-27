package movlit.be.acceptance.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TableCleanup {

    @PersistenceContext
    private EntityManager entityManager;
    private String tableName;

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Transactional
    public void execute() {
        entityManager.flush();
        entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
    }

}
