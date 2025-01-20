package movlit.be.member.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.pub_sub.chatRoom.entity.MemberRChatroom;

@Entity
@NoArgsConstructor
@Table(name = "member")
@ToString
public class MemberEntity {

    @EmbeddedId
    @Getter
    private MemberId memberId;

    @Column(unique = true, nullable = false)
    @Getter
    private String email;

    @Getter
    private String nickname;

    @Getter
    private String password;

    @Getter
    private String dob;

    @Getter
    private String profileImgId;

    @Getter
    private String profileImgUrl;

    @Getter
    private String role;

    @Getter
    private String provider;

    @Getter
    private LocalDateTime regDt;

    @Getter
    private LocalDateTime updDt;

    @Embedded
    private MemberGenres memberGenres; // 일급 컬렉션

    @Getter
    private boolean delYn;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberRChatroom memberRChatroom;

    @Builder
    public MemberEntity(MemberId memberId, String email, String nickname, String password, String dob,
                        String profileImgId,
                        String profileImgUrl, String role, String provider, LocalDateTime regDt, LocalDateTime updDt,
                        List<MemberGenreEntity> memberGenreEntityList, boolean delYn) {
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
        this.memberGenres = new MemberGenres(memberGenreEntityList);
        this.delYn = delYn;
    }

    public void updateMember(Member member, List<MemberGenreEntity> memberGenreEntityList) {
        this.nickname = member.getNickname();
        this.password = member.getPassword();
        // TODO: profileImgId(), profileImgUrl()
        this.dob = member.getDob();
        this.updDt = member.getUpdDt();
        this.memberGenres.replaceWith(memberGenreEntityList);
    }

    public List<MemberGenreEntity> getMemberGenreEntityList() {
        return memberGenres.getNewUnmodifiedList();
    }

}
