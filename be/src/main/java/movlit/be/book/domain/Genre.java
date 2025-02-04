package movlit.be.book.domain;

import java.util.Arrays;
import movlit.be.common.exception.InvalidGenreIdException;

public enum Genre {
    ACTION(1L, "액션"), // + 모험
    ANIMATION(2l, "애니메이션"),
    COMEDY(3L, "코미디"),
    CRIME(4L, "범죄"),
    DOCUMENTARY(5L, "다큐멘터리"),
    DRAMA(6L, "드라마"), // + 가족
    FANTASY(7L, "판타지"),
    HISTORY(8L, "역사"),
    MUSIC(9L, "음악"),
    MYSTERY(10L, "미스터리"),
    ROMANCE(11L, "로맨스"),
    SCIENCE_FICTION(12L, "SF"),
    TV_MOVIE(13L, "TV 영화"),
    THRILLER(14L, "공포, 스릴러"), // + 공포
    WAR(15L, "전쟁"),
    WESTERN(16L, "서부"),
    ETC(99999L, "ETC");

    private final Long id;
    private final String name;

    // Id 값으로 값 접근
    public static Genre of(final Long id) {
        return Arrays.stream(Genre.values())
                .filter(g -> id == g.getId())
                .findFirst().orElseThrow(InvalidGenreIdException::new);
    }

    Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
