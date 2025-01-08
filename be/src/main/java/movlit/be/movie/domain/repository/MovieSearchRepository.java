package movlit.be.movie.domain.repository;

import movlit.be.book.domain.Genre;
import movlit.be.movie.domain.document.MovieDocument;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public interface MovieSearchRepository {
    SearchHits<MovieDocument> searchByUserInterestGenre(List<Genre> genreList);
}
