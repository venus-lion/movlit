package movlit.be.member.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.exception.MemberNotFoundException;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.converter.MemberConverter;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.MemberGenre;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.repository.MemberRepository;
import movlit.be.member.infra.persistence.jpa.MemberGenreJpaRepository;
import movlit.be.member.infra.persistence.jpa.MemberJpaRepository;
import movlit.be.member.presentation.dto.response.GenreListReadResponse;
import movlit.be.member.presentation.dto.response.MemberReadMyPage;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public MemberEntity saveEntity(MemberEntity memberEntity) {
        return memberJpaRepository.save(memberEntity);
    }

    @Override
    public Member save(Member member) {
        MemberEntity memberEntity = MemberConverter.toEntity(member);
        memberJpaRepository.save(memberEntity);
        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public Member findById(MemberId memberId) {
        MemberEntity memberEntity = memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public MemberEntity findEntityById(MemberId memberId) {
        return memberJpaRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Member findByEmail(String email) {
        MemberEntity memberEntity = memberJpaRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);

        return MemberConverter.toDomain(memberEntity);
    }

    @Override
    public void deleteById(MemberId memberId) {
        memberJpaRepository.deleteById(memberId);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return memberJpaRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existByMemberId(MemberId memberId) {
        return memberJpaRepository.existsById(memberId);
    }

    @Override
    public MemberReadMyPage fetchMyPageByMemberId(MemberId memberId) {
        return memberJpaRepository.findMyPageByMemberId(memberId);
    }

}
