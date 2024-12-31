package movlit.be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MemberId;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {

    @EmbeddedId
    private MemberId memberId;

    @Column(unique = true, nullable = false)
    private String email;
    private String nickname;
    private String password;
    private String dob;
    private String profileImgId;
    private String profileImgUrl;
    private String role;
    private String provider;

    private LocalDateTime regDt;
    private LocalDateTime updDt;

    @OneToMany(mappedBy = "memberEntity")
    private List<MemberGenreEntity> memberGenreEntityList = new ArrayList<>();

    @Builder
    public MemberEntity(MemberId memberId, String email, String nickname, String password, String dob,
                        String profileImgId,
                        String profileImgUrl, String role, String provider, LocalDateTime regDt, LocalDateTime updDt,
                        List<MemberGenreEntity> memberGenreEntityList) {
        this.memberId = memberId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.dob = dob;
        this.profileImgId = profileImgId;
        this.profileImgUrl = profileImgUrl;
        this.role = role;
        this.provider = provider;
        this.regDt = regDt;
        this.updDt = updDt;
        this.memberGenreEntityList = memberGenreEntityList;
    }

}
