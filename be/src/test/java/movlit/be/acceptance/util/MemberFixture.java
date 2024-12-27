package movlit.be.acceptance.util;

import java.util.Map;

public enum MemberFixture {

    사용자_민지("mj@naver.com", "Tbaaa123!", "김민지", "2001-06-15"),
    사용자_원준("wj@gmail.com", "Bttta123!", "정원준", "2001-06-14"),
    사용자_윤기("yk@outlook.com", "Cdddd123!", "민윤기", "2001-06-16"),
    사용자_지원("gw@icloud.com", "Cddddd123!", "허지원", "2001-06-13");

    private String email;
    private String password;
    private String nickname;
    private String dob;
//    private List<MemberRGenre> genre;

    MemberFixture(String email, String password, String nickname, String dob) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.dob = dob;
    }

    public static Map<String, Object> 사용자_민지_회원가입_요청() {
        return Map.of(
                "nickname", 사용자_민지.getNickname(),
                "email", 사용자_민지.getEmail(),
                "password", 사용자_민지.getPassword(),
                "repeatPassword", 사용자_민지.getPassword(),
                "dob", 사용자_민지.getDob());
    }

    public static Map<String, Object> 사용자_원준_회원가입_요청() {
        return Map.of(
                "nickname", 사용자_원준.getNickname(),
                "email", 사용자_원준.getEmail(),
                "password", 사용자_원준.getPassword(),
                "repeatPassword", 사용자_원준.getPassword(),
                "dob", 사용자_원준.getDob());
    }

    public static Map<String, Object> 사용자_윤기_회원가입_요청() {
        return Map.of(
                "nickname", 사용자_윤기.getNickname(),
                "email", 사용자_윤기.getEmail(),
                "password", 사용자_윤기.getPassword(),
                "repeatPassword", 사용자_윤기.getPassword(),
                "dob", 사용자_윤기.getDob());
    }

    public static Map<String, Object> 사용자_지원_회원가입_요청() {
        return Map.of(
                "nickname", 사용자_지원.getNickname(),
                "email", 사용자_지원.getEmail(),
                "password", 사용자_지원.getPassword(),
                "repeatPassword", 사용자_지원.getPassword(),
                "dob", 사용자_지원.getDob());
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getDob() {
        return dob;
    }

}
