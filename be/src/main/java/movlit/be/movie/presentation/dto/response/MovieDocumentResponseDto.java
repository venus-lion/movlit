package movlit.be.movie.presentation.dto.response;

import java.util.List;
import movlit.be.movie.domain.document.MovieDocument;

public record MovieDocumentResponseDto(List<MovieDocument> movieList) {
}
