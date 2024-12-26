package movlit.be.member.application.converter;

import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;

public class MemberConverter {

    private MemberConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static MemberEntity toEntity(Member member) {
        return MemberEntity.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .dob(member.getDob())
                .profileImgId(member.getProfileImgId())
                .profileImgUrl(member.getProfileImgUrl())
                .role(member.getRole())
                .provider(member.getProvider())
                .regDt(member.getRegDt())
                .updDt(member.getUpdDt())
                .build();
    }

    // Entity -> Domain
    public static Member toDomain(MemberEntity memberEntity) {
        return Member.builder()
                .memberId(memberEntity.getMemberId())
                .email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname())
                .dob(memberEntity.getDob())
                .profileImgId(memberEntity.getProfileImgId())
                .profileImgUrl(memberEntity.getProfileImgUrl())
                .role(memberEntity.getRole())
                .provider(memberEntity.getProvider())
                .regDt(memberEntity.getRegDt())
                .updDt(memberEntity.getUpdDt())
                .build();
    }

}
