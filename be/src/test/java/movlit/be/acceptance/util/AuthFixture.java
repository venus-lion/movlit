package movlit.be.acceptance.util;

import static movlit.be.acceptance.util.MemberFixture.회원_민지;
import static movlit.be.acceptance.util.MemberFixture.회원_원준;
import static movlit.be.acceptance.util.MemberFixture.회원_윤기;
import static movlit.be.acceptance.util.MemberFixture.회원_지원;

import java.util.Map;

public class AuthFixture {

    public static Map<String, Object> 비회원_로그인_요청() {
        return Map.of(
                "email", "idk@naver.ocm",
                "password", 회원_원준.getPassword());
    }

    public static Map<String, Object> 사용자_원준_로그인_요청() {
        return Map.of(
                "email", 회원_원준.getEmail(),
                "password", 회원_원준.getPassword());
    }

    public static Map<String, Object> 사용자_민지_로그인_요청() {
        return Map.of(
                "email", 회원_민지.getEmail(),
                "password", 회원_민지.getPassword());
    }

    public static Map<String, Object> 사용자_윤기_로그인_요청() {
        return Map.of(
                "email", 회원_윤기.getEmail(),
                "password", 회원_윤기.getPassword());
    }

    public static Map<String, Object> 사용자_지원_로그인_요청() {
        return Map.of(
                "email", 회원_지원.getEmail(),
                "password", 회원_지원.getPassword());
    }

}
