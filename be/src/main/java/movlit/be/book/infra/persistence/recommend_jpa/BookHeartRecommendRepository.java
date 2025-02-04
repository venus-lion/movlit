package movlit.be.book.infra.persistence.recommend_jpa;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MemberId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookHeartRecommendRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<String> findRecentLikedBookIdsByMemberId(MemberId memberId, int count) {
        String sql = "SELECT bh.book_id " +
                "FROM book_heart bh " +
                "JOIN member m ON m.id = bh.member_id " +
                "WHERE m.id = ? " +
                "ORDER BY bh.created_at ASC " +
                "LIMIT ?";
        return jdbcTemplate.queryForList(sql, String.class, memberId.getValue(), count);
    }

}
