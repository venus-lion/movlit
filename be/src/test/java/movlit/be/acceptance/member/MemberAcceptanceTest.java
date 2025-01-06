package movlit.be.acceptance.member;

import static movlit.be.acceptance.auth.AuthSteps.원준이_로그인한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_유효하지_않은_이메일을_입력하고_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_일치하지_않는_재입력_패스워드로_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_회원_원준의_닉네임으로_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_회원_원준의_이메일로_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_200이고_응답에_genreId와_genreName이_존재한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_200이다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_201이고_응답에_memberId가_존재한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_204이다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_400이고_오류코드가_g001이고_errors에_email이_존재하는지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_400이고_오류코드가_g001인지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_404이고_오류코드가_m002인지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_404이고_오류코드가_m003인지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_404이다;
import static movlit.be.acceptance.member.MemberSteps.원준_회원을_수정을_요청한다;
import static movlit.be.acceptance.member.MemberSteps.회원_마이페이지를_조회한다;
import static movlit.be.acceptance.member.MemberSteps.회원_장르를_조회한다;
import static movlit.be.acceptance.member.MemberSteps.회원탈퇴한다;
import static movlit.be.acceptance.movie_comment.MovieCommentSteps.영화_코멘트_작성을_요청한다;

import movlit.be.acceptance.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("회원 관련 기능 인수테스트")
class MemberAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("회원 가입 인수테스트")
    class RegisterMember {

        @DisplayName("회원 가입 시 성공하면, 상태코드 200과 id를 반환하고 회원 프로필이 조회된다.")
        @Test
        void when_signupMember_then_response200AndId_and_canFetchMemberProfile() {
            // docs
            api_문서_타이틀("signupMember_success", spec);

            // when
            var response = 비회원이_회원가입한다(spec);

            // then
            상태코드가_201이고_응답에_memberId가_존재한다(response);
        }

        @DisplayName("회원 가입 시 잘못된 입력값을 입력하면, 상태코드 400과 오류코드 g001를 응답한다")
        @Test
        void when_registerMemberFailedByMultipleInvalidInput_then_response400Andm004() {
            // docs
            api_문서_타이틀("signupMember_failedByMultipleInvalidInput", spec);

            // when
            var response = 비회원이_유효하지_않은_이메일을_입력하고_회원가입한다(spec);

            // then
            상태코드가_400이고_오류코드가_g001이고_errors에_email이_존재하는지_검증한다(response);
        }

        @DisplayName("회원 가입 시 이미 가입된 이메일이면, 상태코드 404와 오류코드 m002를 응답한다")
        @Test
        void when_signupMemberFailedByDuplicateEmail_then_response400Andm002() {
            // docs
            api_문서_타이틀("signupMember_failedByDuplicateEmail", spec);

            // when
            var response = 비회원이_회원_원준의_이메일로_회원가입한다(spec);

            // then
            상태코드가_404이고_오류코드가_m002인지_검증한다(response);
        }

        @DisplayName("회원 가입 시 이미 가입된 닉네임이면, 상태코드 404와 오류코드 m003를 응답한다")
        @Test
        void when_registerMemberFailedByDuplicateNickname_then_response400Andm003() {
            // docs
            api_문서_타이틀("signupMember_failedByDuplicateNickname", spec);

            // when
            var response = 비회원이_회원_원준의_닉네임으로_회원가입한다(spec);

            // then
            상태코드가_404이고_오류코드가_m003인지_검증한다(response);
        }

        @DisplayName("회원 가입 시 재입력한 패스워드가 일치하지 않으면, 상태코드 400과 오류코드 g001을 응답한다")
        @Test
        void when_registerMemberFailedByReconfirmPasswordUnmatch_then_response400Andm004() {
            // docs
            api_문서_타이틀("signupMember_failedByReconfirmPasswordUnmatch", spec);

            // when
            var response = 비회원이_일치하지_않는_재입력_패스워드로_회원가입한다(spec);

            // then
            상태코드가_400이고_오류코드가_g001인지_검증한다(response);
        }

    }

    @Nested
    @DisplayName("회원 탈퇴 인수테스트")
    class DeleteMember {

        @DisplayName("회원 탈퇴 시 성공하면, 상태코드 404를 반환한다.")
        @Test
        void when_signupMember_then_response200AndId_and_canFetchMemberProfile() {
            // docs
            api_문서_타이틀("deleteMember_success", spec);

            // given
            String accessToken = 회원_원준_액세스토큰;

            // when
            var response = 회원탈퇴한다(accessToken, spec);
            var 탈퇴한_원준_응답 = 원준이_로그인한다(spec);

            // then
            상태코드가_204이다(response);
            상태코드가_404이다(탈퇴한_원준_응답);
        }

    }

    @Nested
    @DisplayName("회원 수정 인수테스트")
    class UpdateMember {

        String accessToken;

        @BeforeEach
        public void before() {
            accessToken = 회원_원준_액세스토큰;
        }

        @DisplayName("회원 가입 시 성공하면, 상태코드 200과 id를 반환하고 회원 프로필이 조회된다.")
        @Test
        void when_update_member_then_response_200() {
            // docs
            api_문서_타이틀("updateMember_success", spec);

            // when
            var response = 원준_회원을_수정을_요청한다(accessToken, spec);

            // then
            상태코드가_200이다(response);
        }

    }

    @Nested
    @DisplayName("장르 조회 인수 테스트")
    class ReadMember {

        @DisplayName("장르의 조회가 성공하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_read_genre_success_then_response_200_and_body() {
            // docs
            api_문서_타이틀("genreRead_success", spec);

            // when
            var response = 회원_장르를_조회한다(spec);

            // then
            상태코드가_200이고_응답에_genreId와_genreName이_존재한다(response);
        }

        @DisplayName("회원 장르의 조회가 성공하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_read_member_genre_success_then_response_200_and_body() {
            // docs
            api_문서_타이틀("memberGenreRead_success", spec);

            // given
            String accessToken = 회원_원준_액세스토큰;

            // when
            var response = 회원_장르를_조회한다(spec, accessToken);

            // then
            상태코드가_200이고_응답에_genreId와_genreName이_존재한다(response);
        }

    }

    @Nested
    @DisplayName("마이 페이지 조회 인수 테스트")
    class MyPageRead {

        private String accessToken;

        @BeforeEach
        public void before() {
            accessToken = 회원_원준_액세스토큰;
            String movieId = String.valueOf(767L);
            영화_코멘트_작성을_요청한다(accessToken, movieId, spec);
            // TODO: 영화 찜 테스트 구현되면 여기에 찜 요청 추가
        }

        @DisplayName("회원 장르의 조회가 성공하면, 상태코드 200과 body를 반환한다.")
        @Test
        void when_read_member_genre_success_then_response_200_and_body() {
            // docs
            api_문서_타이틀("myPageRead_success", spec);

            // when
            var response = 회원_마이페이지를_조회한다(spec, accessToken);

            // then
            상태코드가_200이다(response);
        }

    }

}
