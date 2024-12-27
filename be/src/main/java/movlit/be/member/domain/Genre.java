package movlit.be.member.domain;

public enum Genre {

    ACTION(1, "액션"), // + 모험
    ANIMATION(2, "애니메이션"),
    COMEDY(3, "코미디"),
    CRIME(4, "범죄"),
    DOCUMENTARY(5, "다큐멘터리"),
    DRAMA(6, "드라마"), // + 가족
    FANTASY(7, "판타지"),
    HISTORY(8, "역사"),
    MUSIC(9, "음악"),
    MYSTERY(10, "미스터리"),
    ROMANCE(11, "로맨스"),
    SCIENCE_FICTION(12, "SF"),
    TV_MOVIE(13, "TV 영화"),
    THRILLER(14, "스릴러"), // + 공포
    WAR(15, "전쟁"),
    WESTERN(16, "서부");

    private final int id;
    private final String name;

    Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
