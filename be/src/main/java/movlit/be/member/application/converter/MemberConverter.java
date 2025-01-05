package movlit.be.member.application.converter;

import java.time.LocalDateTime;
import java.util.List;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.entity.MemberGenreIdEntity;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MemberConverter {

    private MemberConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static MemberEntity toEntity(Member member) {
        return MemberEntity.builder()
                .memberId(member.getMemberId())
                .memberGenreEntityList(member.getMemberGenreEntityList())
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
                .password(memberEntity.getPassword())
                .dob(memberEntity.getDob())
                .profileImgId(memberEntity.getProfileImgId())
                .profileImgUrl(memberEntity.getProfileImgUrl())
                .role(memberEntity.getRole())
                .provider(memberEntity.getProvider())
                .regDt(memberEntity.getRegDt())
                .updDt(memberEntity.getUpdDt())
                .build();
    }

    // Domain to Dto
    public static MemberRegisterResponse toRegisterResponse(Member member) {
        return MemberRegisterResponse.from(member.getMemberId());
    }

    public static MemberGenreEntity toMemberGenreEntityList(Long genreId, MemberId memberId) {
        return new MemberGenreEntity(new MemberGenreIdEntity(genreId, memberId));
    }

    public static MemberEntity toMemberEntity(MemberRegisterRequest request, List<MemberGenreEntity> memberGenreEntityList,
                                              MemberId memberId) {
        return MemberEntity.builder()
                .memberId(memberId)
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .memberGenreEntityList(memberGenreEntityList)
                .nickname(request.getNickname())
                .email(request.getEmail())
                .dob(request.getDob())
                .regDt(LocalDateTime.now())
                .role("ROLE_Member")
                .provider("local")
                .build();
    }

    public static MemberRegisterResponse toMemberRegisterResponse(MemberId memberId) {
        return MemberRegisterResponse.from(memberId);
    }

}
