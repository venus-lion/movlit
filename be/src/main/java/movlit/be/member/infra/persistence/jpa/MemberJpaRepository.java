package movlit.be.member.infra.persistence.jpa;

import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.presentation.dto.response.MemberReadMyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, MemberId> {

    @Query("SELECT m "
            + "FROM MemberEntity m "
            + "WHERE m.memberId = :memberId AND m.delYn = false")
    Optional<MemberEntity> findByMemberId(@Param("memberId") MemberId memberId);

    @Query("SELECT m "
            + "FROM MemberEntity m "
            + "WHERE m.email = :email AND m.delYn = false")
    Optional<MemberEntity> findByEmail(@Param("email") String email);

    @Query(
            "SELECT EXISTS(SELECT 1 "
                    + "FROM MemberEntity m "
                    + "WHERE m.nickname = :nickname AND m.delYn = false)"
    )
    boolean existsByNickname(@Param("nickname") String nickname);

    @Query(
            "SELECT EXISTS(SELECT 1 "
                    + "FROM MemberEntity m "
                    + "WHERE m.email = :email AND m.delYn = false)"
    )
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT NEW movlit.be.member.presentation.dto.response.MemberReadMyPage(i.url, mb.nickname, mb.email, "
            + "(SELECT COUNT(mh) FROM MovieHeartEntity mh WHERE mh.memberId = mb.memberId), "
            + "(SELECT COUNT(mc) FROM MovieCommentEntity mc WHERE mc.memberId = mb.memberId), "
            + "(SELECT COUNT(bh) FROM BookHeartEntity bh WHERE bh.memberEntity.memberId = mb.memberId), "
            + "(SELECT COUNT(bc) FROM BookCommentEntity bc WHERE bc.memberEntity.memberId = mb.memberId)) "
            + "FROM MemberEntity mb "
            + "LEFT JOIN ImageEntity i ON i.memberId = mb.memberId "
            + "WHERE mb.memberId = :memberId")
    MemberReadMyPage findMyPageByMemberId(@Param("memberId") MemberId memberId);

    @Modifying
    @Query("UPDATE MemberEntity m "
            + "SET m.delYn = true "
            + "WHERE m.memberId = :memberId")
    void softDeleteByMemberId(@Param("memberId") MemberId memberId);

}
