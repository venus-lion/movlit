package movlit.be.movie.infra.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
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
                    TermQuery.of(t -> t
                            .field("movieGenre.genreId")
                            .value(genre.getId())
                    )._toQuery()
            );
        }
        BoolQuery boolQuery = boolQueryBuilder.build(); // BoolQuery 빌더를 사용하여 BoolQuery 생성

        Query nestedQuery = Query.of(q -> q
                .nested(n -> n
                        .path("movieGenre")
                        .query(q1 -> q1.bool(boolQuery)))
        );

        // FunctionScoreQuery: 각 장르에 대해 가중치 설정
        List<FunctionScore> functions = new ArrayList<>();
        genreList.forEach(genre ->
                functions.add(FunctionScore.of(f -> f
                        .filter(Query.of(q -> q.term(t -> t
                                .field("movieGenre.genreId")
                                .value(genre.getId()) // genreId 값
                        )))
                        .weight(1.5) // 각 장르에 대해 가중치 1.5
                ))
        );

        Query functionScoreQuery = Query.of(q -> q.functionScore(fs -> fs
                .query(nestedQuery) // NestedQuery 기반
                .functions(functions) // 가중치 추가
                .scoreMode(FunctionScoreMode.Sum) // 점수 합산
                .boostMode(FunctionBoostMode.Sum) // 부스트 합산
        ));


        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(functionScoreQuery)
                .withPageable(pageable)
                .withSort(Sort.by(
                        Sort.Order.desc("_score"),
                        Sort.Order.desc("voteAverage")      // score순, 평점 순
                ))
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
