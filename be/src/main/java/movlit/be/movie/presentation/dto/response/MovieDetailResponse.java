package movlit.be.movie.presentation.dto.response;

import java.time.LocalDate;

public record MovieDetailResponse(Long movieId, String title, String originalTitle, String overview,
                                  Double popularity, Long heartCount, boolean isHearted, String posterPath,
                                  String backdropPath, LocalDate releaseDate, String productionCountry,
                                  String originalLanguage, Integer runtime, String status, Double voteAverage,
                                  String tagline) {

}
