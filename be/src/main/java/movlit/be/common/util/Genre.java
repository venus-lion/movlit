package movlit.be.common.util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import movlit.be.common.exception.InvalidGenreIdException;
import movlit.be.member.presentation.dto.response.GenreListReadResponse;

public enum Genre {

    ACTION(1L, "액션"), // + 모험
    ANIMATION(2L, "애니메이션"),
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
                .filter(g -> Objects.equals(id, g.getId()))
                .findFirst().orElseThrow(InvalidGenreIdException::new);
    }

    Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<GenreListReadResponse> getGenreList() {
        return Arrays.stream(Genre.values())
                .map(genre -> new GenreListReadResponse(genre.id, genre.name))
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
