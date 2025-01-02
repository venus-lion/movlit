package movlit.be.member.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGenreEntity {

    @EmbeddedId
    private MemberGenreIdEntity memberGenreIdEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", updatable = false, insertable = false)
    private MemberEntity memberEntity;

}
