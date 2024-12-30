package movlit.be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGenreEntity {

    @Id
    @Column(name = "genre_id")
    private Long genreId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private MemberEntity memberEntity;

}
