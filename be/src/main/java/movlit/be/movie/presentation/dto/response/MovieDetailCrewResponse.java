package movlit.be.movie.presentation.dto.response;

import movlit.be.movie.domain.MovieRole;

public record MovieDetailCrewResponse(String profileImgUrl, String name, String charName, MovieRole role) {

}
