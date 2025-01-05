package movlit.be.acceptance.member;

import static movlit.be.acceptance.member.MemberSteps.비회원이_유효하지_않은_이메일을_입력하고_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_일치하지_않는_재입력_패스워드로_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_회원_원준의_닉네임으로_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_회원_원준의_이메일로_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.비회원이_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_200이고_응답에_genreId와_genreName이_존재한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_201이고_응답에_memberId가_존재한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_400이고_오류코드가_g001이고_errors에_email이_존재하는지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_400이고_오류코드가_g001인지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_404이고_오류코드가_m002인지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_404이고_오류코드가_m003인지_검증한다;
import static movlit.be.acceptance.member.MemberSteps.회원_장르를_조회한다;

import movlit.be.acceptance.AcceptanceTest;
import movlit.be.common.util.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("회원 관련 기능 인수테스트")
class MemberAcceptanceTest extends AcceptanceTest {

    @Autowired
    JwtTokenUtil jwtUtil;

    @Nested
    @DisplayName("회원 가입 인수테스트")
    class SignUp {

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
    @DisplayName("장르 조회 인수 테스트")
    class MemberRead {

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


}
