package movlit.be.member.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberGenreEntity {

    @EmbeddedId
    private MemberGenreIdEntity memberGenreIdEntity;

    public MemberGenreEntity(MemberGenreIdEntity memberGenreIdEntity) {
        this.memberGenreIdEntity = memberGenreIdEntity;
    }

}
