package movlit.be.movie.infra.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Repository
@RequiredArgsConstructor
@Slf4j
public class MovieSearchRepositoryImpl implements MovieSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public List<Movie> searchByUserInterestGenre(List<Genre> genreList, Pageable pageable) {

        // NativeQuery 작성
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder(); // BoolQuery 생성에 사용할 빌더 생성

        for (Genre genre : genreList) {
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
                        Sort.Order.desc("_score")
//                        Sort.Order.desc("voteAverage")      // score순, 평점 순
                ))
                .build();

        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);
        List<Movie> movieList = this.getMovieDocumentResult(searchHits);

        return movieList;

    }

    @Override
    public List<Movie> searchByUserHeartMovie(Movie movie, Pageable pageable) {
        log.info("==== recent heart movie :{}", movie);
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 첫 번째 MatchQuery: title에 대한 쿼리와 boost 1.5 설정
        Query titleMatchQuery = MatchQuery.of(t -> t
                .field("overview")
                .query(movie.getTitle())
                .boost(1.5f) // boost 값 추가
        )._toQuery();

        // 두 번째 MatchQuery: tagline에 대한 쿼리와 boost 2.0 설정
        Query taglineMatchQuery = MatchQuery.of(t -> t
                .field("overview")
                .query(movie.getTagline())
                .boost(2.0f) // boost 값 추가
        )._toQuery();

        // 제외할 조건 (must_not 절): 특정 단어를 포함하지 않는 문서
        // TermQuery: 해당 movie는 제거
        Query excludeMovieIdQuery = TermQuery.of(t -> t
                .field("movieId")
                .value(movie.getMovieId()) // 해당 movie는 제거
        )._toQuery();

        // BoolQuery에 MatchQuery 추가
        boolQueryBuilder
                .should(titleMatchQuery)
                .should(taglineMatchQuery)
                .mustNot(excludeMovieIdQuery)
                .minimumShouldMatch("1");

        // BoolQuery 빌드
        BoolQuery boolQuery = boolQueryBuilder.build();

        // NativeSearchQuery 빌드
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(boolQuery._toQuery())
                .withPageable(pageable)
                .withSort(Sort.by(
                        Sort.Order.desc("_score")
                ))
                .build();

        // 검색 실행
        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);
        List<Movie> movieList = this.getMovieDocumentResult(searchHits);

        return movieList;
    }

    private List<Movie> getMovieDocumentResult(SearchHits<MovieDocument> searchHits) {
        List<Movie> movieList = new ArrayList<>();

        // 값이 없을경우
        if (!searchHits.hasSearchHits()) {
            return movieList;
        }

        movieList = searchHits.stream()
                .map(hit -> MovieDocumentConverter.documentToDomain(hit.getContent()))
                .toList();
        return movieList;
    }

}
