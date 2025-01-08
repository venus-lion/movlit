package movlit.be.movie.infra.persistence;

import movlit.be.book.domain.Genre;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class MovieSearchRepositoryImpl implements MovieSearchRepository {

    @Override
    public SearchHits<MovieDocument> searchByUserInterestGenre(List<Genre> genreList) {

        return null;
    }
}
