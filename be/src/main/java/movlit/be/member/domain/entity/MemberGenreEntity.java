package movlit.be.member.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberGenreId;
import movlit.be.common.util.ids.MemberId;

@Entity
@Table(name = "member_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberGenreEntity {

    @EmbeddedId
    private MemberGenreId memberGenreId;

    private Long genreId;

    @AttributeOverride(name = "value", column = @Column(name = "member_id"))
    private MemberId memberId;

    public MemberGenreEntity(Long genreId, MemberId memberId) {
        this.memberGenreId = IdFactory.createMemberGenreId();
        this.genreId = genreId;
        this.memberId = memberId;
    }

}
