package movlit.be.member.infra.persistence.jpa;

import java.util.List;
import java.util.Optional;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.MemberGenreId;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.presentation.dto.response.MemberReadMyPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, MemberId> {

    //@Query("SELECT m FROM MemberEntity m WHERE m.email = :email")
    Optional<MemberEntity> findByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    // TODO: Book 정보 추가
    @Query("SELECT NEW movlit.be.member.presentation.dto.response.MemberReadMyPage(mb.profileImgUrl, mb.nickname, mb.email, "
            + "(SELECT COUNT(mh) FROM MovieHeartEntity mh WHERE mh.memberId = mb.memberId), "
            + "(SELECT COUNT(mh) FROM MovieCommentEntity mc WHERE mc.memberId = mb.memberId)) "
            + "FROM MemberEntity mb "
            + "LEFT JOIN MovieHeartEntity mh ON mh.memberId = mb.memberId "
            + "LEFT JOIN MovieCommentEntity mc ON mc.memberId = mb.memberId "
            + "WHERE mb.memberId = :memberId")
    MemberReadMyPage findMyPageByMemberId(MemberId memberId);

}
