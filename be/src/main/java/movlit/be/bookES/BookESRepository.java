package movlit.be.bookES;

import movlit.be.common.util.ids.BookId;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "movlit.be.bookES")
public interface BookESRepository extends ElasticsearchRepository<BookES, Long> {

}
