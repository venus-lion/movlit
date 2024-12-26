package movlit.be.member.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name="member")
public class MemberEntity {
    @Id         // primary key
    private String memberId;

    private String email;
    private String nickname;
    private String password;
    private String dob; // 생년월일
    private String profileImgId;
    private String profileImgUrl;
    private String role;
    private String provider;

    private LocalDateTime regDt;
    private LocalDateTime updDt;

    @Builder
    public MemberEntity(String memberId, String email, String nickname, String password, String dob, String profileImgId,
                  String profileImgUrl, String role, String provider, LocalDateTime regDt, LocalDateTime updDt) {
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
    }
}
