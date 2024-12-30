package movlit.be.movie.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.movie.domain.MovieRole;

@Table(name = "movie_crew")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MovieCrewEntity {

    /**
     * cast(배우): order로 정렬 (우선순위)
     * crew(스태프): 감독만 생각 (첫 번째 배열)
     */
    @EmbeddedId
    private MovieCrewId movieCrewId;
    private String name;

    @Enumerated(EnumType.STRING)
    private MovieRole role;
    private String charName;
    private String profileImgUrl;
    private int orderNo; // 감독: -1, 배우: 0부터 정렬

    @OneToMany(mappedBy = "movieCrewEntity")
    private List<MovieRCrewEntity> movieRCrewEntities = new ArrayList<>();

    @Builder
    public MovieCrewEntity(MovieCrewId movieCrewId, String name, MovieRole role, String charName, String profileImgUrl,
                           int orderNo, List<MovieRCrewEntity> movieRCrewEntities) {
        this.movieCrewId = movieCrewId;
        this.name = name;
        this.role = role;
        this.charName = charName;
        this.profileImgUrl = profileImgUrl;
        this.orderNo = orderNo;
        this.movieRCrewEntities = movieRCrewEntities;
    }

}
