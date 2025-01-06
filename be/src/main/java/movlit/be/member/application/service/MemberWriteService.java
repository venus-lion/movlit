package movlit.be.member.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.auth.application.service.AuthTokenService;
import movlit.be.auth.application.service.dto.AuthTokenIssueResponse;
import movlit.be.common.exception.DuplicateEmailException;
import movlit.be.common.exception.DuplicateNicknameException;
import movlit.be.common.exception.MemberPasswordMismatchException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.UniqueNicknameGenerator;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.presentation.dto.request.MemberLoginRequest;
import movlit.be.member.presentation.dto.request.MemberRegisterOAuth2Request;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.request.MemberUpdateRequest;
import movlit.be.member.presentation.dto.response.MemberRegisterOAuth2Response;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberWriteService {

    private final MemberRepository memberRepository;
    private final MemberReadService memberReadService;
    private final AuthTokenService authTokenService;

    public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
        MemberId memberId = IdFactory.createMemberId();
        List<MemberGenreEntity> memberGenreEntityList = makeMemberGenreEntities(
                memberId, request.getGenreIds());
        MemberEntity memberEntity = MemberConverter.toMemberEntity(request, memberGenreEntityList, memberId);

        validateNicknameDuplication(memberEntity.getNickname());
        validateEmailDuplication(memberEntity.getEmail());

        MemberEntity savedMemberEntity = memberRepository.saveEntity(memberEntity);
        return MemberConverter.toMemberRegisterResponse(savedMemberEntity.getMemberId());
    }

    public MemberRegisterOAuth2Response registerOAuth2Member(MemberRegisterOAuth2Request request) {
//        Member member = MemberConverter.oAuth2RequestToMemberEntity(request, UniqueNicknameGenerator.generate());
//        Member savedMember = memberRepository.save(member);
//        return MemberConverter.toMemberRegisterOAuth2Response(savedMember.getMemberId());
        return MemberRegisterOAuth2Response.from(IdFactory.createMemberId());
    }

    public void updateMember(MemberId memberId, MemberUpdateRequest request) {
        MemberEntity memberEntity = memberRepository.findEntityById(memberId);
        Member member = MemberConverter.toMemberForUpdate(request);
        memberEntity.updateMember(member, makeMemberGenreEntities(memberEntity.getMemberId(), request.getGenreIds()));
    }

    private List<MemberGenreEntity> makeMemberGenreEntities(MemberId memberId, List<Long> genreIds) {
        return genreIds.stream()
                .map(genreId -> MemberConverter.toMemberGenreEntity(genreId, memberId))
                .toList();
    }

    private void validateNicknameDuplication(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    private void validateEmailDuplication(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateEmailException();
        }
    }

    public AuthTokenIssueResponse login(MemberLoginRequest request) {
        // TODO: usecase를 안 쓰니 write 서비스에서 read 서비스를 참조하는 일이 생김
        Member member = memberReadService.findByMemberEmail(request.getEmail());
        checkPasswordMatch(request, member);
        return authTokenService.issue(member.getEmail());
    }

    private void checkPasswordMatch(MemberLoginRequest request, Member member) {
        if (!BCrypt.checkpw(request.getPassword(), member.getPassword())) {
            throw new MemberPasswordMismatchException();
        }
    }

    public void logout(String accessToken) {
        authTokenService.revoke(accessToken);
    }

}

