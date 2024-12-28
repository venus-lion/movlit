package movlit.be.acceptance.member;

import static movlit.be.acceptance.member.MemberSteps.비회원_원준이_회원가입한다;
import static movlit.be.acceptance.member.MemberSteps.상태코드가_201이고_응답에_id가_존재하며_회원가입한_보노의_회원프로필이_조회되는지_검증한다;

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
            var response = 비회원_원준이_회원가입한다(spec);

            // then
            상태코드가_201이고_응답에_id가_존재하며_회원가입한_보노의_회원프로필이_조회되는지_검증한다(response);
        }

//        @DisplayName("회원 가입 시 잘못된 입력값을 입력하면, 상태코드 400과 오류코드 g001를 응답한다")
//        @Test
//        void when_registerMemberFailedByMultipleInvalidInput_then_response400Andm004() {
//            // docs
//            api_문서_타이틀("signupMember_failedByMultipleInvalidInput", spec);
//
//            // when
//            var response = 비회원보노가_유효하지_않은_이메일을_입력하고_닉네임을_입력하지_않고_패스워드를_입력하지_않고_회원가입한다(spec);
//
//            // then
//            상태코드가_400이고_오류코드가_g001이고_errors에_email과_nickname과_password가_존재하는지_검증한다(response);
//        }
//
//        @DisplayName("회원 가입 시 이미 가입된 이메일이면, 상태코드 400과 오류코드 m002를 응답한다")
//        @Test
//        void when_signupMemberFailedByDuplicateEmail_then_response400Andm002() {
//            // docs
//            api_문서_타이틀("signupMember_failedByDuplicateEmail", spec);
//
//            // when
//            var response = 비회원보노가_회원푸반의_이메일로_회원가입한다(spec);
//
//            // then
//            상태코드가_400이고_오류코드가_m002인지_검증한다(response);
//        }
//
//        @DisplayName("회원 가입 시 이미 가입된 닉네임이면, 상태코드 400과 오류코드 m003를 응답한다")
//        @Test
//        void when_registerMemberFailedByDuplicateNickname_then_response400Andm003() {
//            // docs
//            api_문서_타이틀("signupMember_failedByDuplicateNickname", spec);
//
//            // when
//            var response = 비회원보노가_회원푸반의_닉네임으로_회원가입한다(spec);
//
//            // then
//            상태코드가_400이고_오류코드가_m003인지_검증한다(response);
//        }
//
//        @DisplayName("회원 가입 시 재입력한 패스워드가 일치하지 않으면, 상태코드 400과 오류코드 g001을 응답한다")
//        @Test
//        void when_registerMemberFailedByReconfirmPasswordUnmatch_then_response400Andm004() {
//            // docs
//            api_문서_타이틀("signupMember_failedByReconfirmPasswordUnmatch", spec);
//
//            // when
//            var response = 비회원보노가_틀린_재입력_패스워드로_회원가입한다(spec);
//
//            // then
//            상태코드가_400이고_오류코드가_g001인지_검증한다(response);
//        }

    }


}
