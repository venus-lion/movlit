package movlit.be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;

@Entity

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberGenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberGenreId;
    private Long genreId;
    private MemberId memberId;

    public MemberGenreEntity(Long genreId, MemberId memberId) {
        this.genreId = genreId;
        this.memberId = memberId;
    }

}
