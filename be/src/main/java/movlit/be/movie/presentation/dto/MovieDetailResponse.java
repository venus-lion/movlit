package movlit.be.movie.presentation.dto;

import java.time.LocalDate;
import movlit.be.common.util.ids.MovieId;

public record MovieDetailResponse(MovieId movieId, String title, String originalTitle, String overview,
                                  Double popularity, Long heartCount, String posterPath, String backdropPath,
                                  LocalDate releaseDate, String productionCountry, String originalLanguage,
                                  Integer runtime, String status, Long voteCount, String tagline) {

}
