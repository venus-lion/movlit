package movlit.be.movie.infra.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.Genre;
import movlit.be.movie.application.converter.main.MovieDocumentConverter;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovieSearchRepositoryImpl implements MovieSearchRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<Movie> searchByUserInterestGenre(List<Genre> genreList, Pageable pageable) {
        List<Movie> movieList = new ArrayList<>();

        // NativeQuery 작성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder(); // BoolQuery 생성에 사용할 빌더 생성

        for(Genre genre : genreList) {
            boolQueryBuilder.should(
                    TermQuery.of(t -> t.field("movieGenre.genreName").value(genre.getName()))._toQuery()
            );
        }

        BoolQuery boolQuery = boolQueryBuilder.build(); // BoolQuery 빌더를 사용하여 BoolQuery 생성
        NestedQuery nestedQuery = NestedQuery.of(n -> n
                .path("movieGenre")
                .query(boolQuery._toQuery())
        );

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(nestedQuery._toQuery())
                .withPageable(pageable)
                .withSort(Sort.by(Sort.Order.desc("_score")))
                .build();

        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);
        // 값이 없을경우
        if (!searchHits.hasSearchHits()) {
            return movieList;
        }

        movieList = searchHits.stream()
                .map(hit -> MovieDocumentConverter.documentToEntity(hit.getContent()))
                .collect(Collectors.toList());

        return movieList;

    }
}
