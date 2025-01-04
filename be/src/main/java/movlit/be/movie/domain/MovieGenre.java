package movlit.be.movie.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.movie.domain.entity.MovieEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieGenre {

    private Long genreId;
    private String genreName;

    // TODO : 아래 필드 확인 필요
//    private MovieGenre movieGenre;
//    private MovieEntity movieEntity;

}
