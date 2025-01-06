package movlit.be.acceptance.util;

import java.util.List;
import java.util.Map;

public enum MemberFixture {

    회원_민지("mj1234@naver.com", "Tbaaa123!", "김민지", "2001-06-15", List.of(1L, 3L, 5L)),
    회원_원준("wj1234@gmail.com", "Bttta123!", "정원준", "2001-06-14", List.of(3L, 6L, 7L)),
    회원_윤기("yk1234@outlook.com", "Cdddd123!", "민윤기", "2001-06-16", List.of(4L, 5L, 8L)),
    회원_지원("gw1234@icloud.com", "Cddddd123!", "허지원", "2001-06-13", List.of(5L, 7L, 9L)),
    비회원("dsakj2133@hanmail.net", "asdAdd1233!", "성이름", "1979-11-13", List.of(1L, 5L, 10L));

    private String email;
    private String password;
    private String nickname;
    private String dob;
    private List<Long> genreIds;

    MemberFixture(String email, String password, String nickname, String dob, List<Long> genreIds) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.dob = dob;
        this.genreIds = genreIds;
    }

    public static Map<String, Object> 사용자_민지_회원가입_요청() {
        return Map.of(
                "nickname", 회원_민지.getNickname(),
                "email", 회원_민지.getEmail(),
                "password", 회원_민지.getPassword(),
                "repeatPassword", 회원_민지.getPassword(),
                "dob", 회원_민지.getDob(),
                "genreIds", 회원_민지.getGenreIds());
    }

    public static Map<String, Object> 사용자_원준_회원가입_요청() {
        return Map.of(
                "nickname", 회원_원준.getNickname(),
                "email", 회원_원준.getEmail(),
                "password", 회원_원준.getPassword(),
                "repeatPassword", 회원_원준.getPassword(),
                "dob", 회원_원준.getDob(),
                "genreIds", 회원_원준.getGenreIds());
    }

    public static Map<String, Object> 사용자_윤기_회원가입_요청() {
        return Map.of(
                "nickname", 회원_윤기.getNickname(),
                "email", 회원_윤기.getEmail(),
                "password", 회원_윤기.getPassword(),
                "repeatPassword", 회원_윤기.getPassword(),
                "dob", 회원_윤기.getDob(),
                "genreIds", 회원_윤기.getGenreIds());
    }

    public static Map<String, Object> 사용자_지원_회원가입_요청() {
        return Map.of(
                "nickname", 회원_지원.getNickname(),
                "email", 회원_지원.getEmail(),
                "password", 회원_지원.getPassword(),
                "repeatPassword", 회원_지원.getPassword(),
                "dob", 회원_지원.getDob(),
                "genreIds", 회원_지원.getGenreIds());
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

    public List<Long> getGenreIds() {
        return genreIds;
    }
}
