package movlit.be.movie.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import movlit.be.common.util.ids.MovieCrewId;

@Getter
public class MovieCrew {

    /**
     * cast(배우): order로 정렬 (우선순위)
     * crew(스태프): 감독만 생각 (첫 번째 배열)
     */
    private MovieCrewId movieCrewId;
    private String name;
    private MovieRole role;
    private String charName;
    private String profileImgUrl;
    private int orderNo; // 감독: -1, 배우: 0부터 정렬
    private List<MovieRCrew> movieRCrewList = new ArrayList<>();

}
