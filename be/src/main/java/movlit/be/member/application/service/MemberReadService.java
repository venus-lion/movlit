package movlit.be.member.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.presentation.dto.response.GenreListReadResponse;
import movlit.be.member.presentation.dto.response.MemberReadMyPage;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberReadService {

    private final MemberRepository memberRepository;

    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int Member_NOT_EXIST = 2;

    public Member findByMemberEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public int login(String email, String pwd) {
        Member member = findByMemberEmail(email);
        // TODO: login 실패 로직 짜기
        if (member == null) {
            return Member_NOT_EXIST;
        }
        if (BCrypt.checkpw(pwd, member.getPassword())) {
            return CORRECT_LOGIN;
        }
        return WRONG_PASSWORD;
    }

    public Member findByMemberId(MemberId memberId) {
        return memberRepository.findById(memberId);
    }

    public void validateMemberIdExists(MemberId memberId) {
        if (!memberRepository.existByMemberId(memberId)) {
            throw new MemberNotFoundException();
        }
    }

    public List<GenreListReadResponse> fetchGenreListById(MemberId memberId) {
        MemberEntity memberEntity = memberRepository.findEntityById(memberId);
        return memberEntity.getMemberGenreEntityList().stream()
                .map(genre -> {
                    Long genreId = genre.getGenreId();
                    String genreName = Genre.of(genreId).getName();
                    return new GenreListReadResponse(genreId, genreName);
                })
                .toList();
    }

    public List<GenreListReadResponse> getGenreList() {
        return Genre.getGenreList();
    }

    public MemberReadMyPage fetchMyPage(MemberId memberId) {
        return memberRepository.fetchMyPageByMemberId(memberId);
    }

}
