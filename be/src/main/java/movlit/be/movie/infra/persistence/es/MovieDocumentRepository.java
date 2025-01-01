package movlit.be.movie.infra.persistence.es;

import movlit.be.movie.domain.document.MovieDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

public interface MovieDocumentRepository extends ElasticsearchRepository<MovieDocument, Long> {

}
