package movlit.be.movie.presentation.dto.response;

import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.movie.domain.MovieRole;

public record MovieCrewResponseDto(
        Long movieId,
        MovieCrewId movieCrewId,
        String name,
        MovieRole role,  // 순서: movieCrewId -> name -> role -> orderNo
        int orderNo
) {
}
