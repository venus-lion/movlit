package movlit.be;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class MovieCrew {

    /**
     * cast(배우): order로 정렬 (우선순위)
     * crew(스태프): 감독만 생각 (첫 번째 배열)
     */
    @Id
    @Column(name = "movie_crew_id")
    @JsonProperty("id")
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private MovieRole role;
    private String charName;
    private String profileImgUrl;
    private int orderNo; // 감독: -1, 배우: 0부터 정렬

    @OneToMany(mappedBy = "movieCrew")
    private List<MovieRCrew> movieRCrews = new ArrayList<>();

    @Builder
    public MovieCrew(Long id, String name, MovieRole role, String charName, String profileImgUrl, int orderNo,
                     List<MovieRCrew> movieRCrews) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.charName = charName;
        this.profileImgUrl = profileImgUrl;
        this.orderNo = orderNo;
        this.movieRCrews = movieRCrews;
    }

}
