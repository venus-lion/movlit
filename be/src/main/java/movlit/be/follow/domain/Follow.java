package movlit.be.follow.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.FollowId;
import movlit.be.member.domain.entity.MemberEntity;

@Entity
@NoArgsConstructor
@Table(name = "follow")
@Getter
@Builder
@AllArgsConstructor
public class Follow {
    @EmbeddedId
    private FollowId followId;


    // Follow 엔디티는 팔로우 관계를 단방향 매핑으로 구현한다
    // 예: "A"가 "B"를 팔로우한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private MemberEntity follower; // 팔로우하는 사용자, "A"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id", nullable = false)
    private MemberEntity followee; // 팔로우를 받는 사용자, "B"
}
