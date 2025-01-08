package movlit.be.movie.infra.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MovieSearchRepositoryImpl implements MovieSearchRepository {

    private final ElasticsearchOperations elasticsearchOperations;

    /**
     * 취향 장르와 일치하는 장르가 있으면 가중치를 부여해서 점수 높은 순으로 가져오기
     */
    @Override
    public List<Movie> searchInterestGenre(List<Genre> genreList, Pageable pageable) {
        // Nested Query 생성
        NestedQuery nestedQuery = NestedQuery.of(n -> n
                .path("movieGenre")
                .query(q -> q.bool(b -> b.should(genreList.stream()
                                .map(genre -> Query.of(query -> query.term(t -> t
                                        .field("movieGenre.genreId")
                                        .value(genre.getId())
                                )))
                                .collect(Collectors.toList()))
                        )
                )
        );
        // FunctionScoreQuery: 각 장르에 대해 가중치 설정
        List<FunctionScore> functions = new ArrayList<>();

        genreList.forEach(genre ->
                functions.add(FunctionScore.of(f -> f
                        .filter(NestedQuery.of(n -> n
                                .path("movieGenre")
                                .query(q -> q.term(t -> t
                                        .field("movieGenre.genreId")
                                        .value(genre.getId())
                                ))
                        )._toQuery())
                        .weight(1.5))
                )
        );

        FunctionScoreQuery functionScoreQuery = FunctionScoreQuery.of(f -> f
                .query(nestedQuery._toQuery())
                .functions(functions)  // 가중치 설정 함수
                .scoreMode(FunctionScoreMode.Sum)
                .boostMode(FunctionBoostMode.Sum)
        );

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(functionScoreQuery._toQuery())
                .withPageable(pageable)
                .withSort(Sort.by(
                        Sort.Order.desc("_score")
                ))
                .build();
        nativeQuery.setExplain(true);

        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);

//        log.info("Explain output: {}", searchHits.getSearchHits().get(0).getExplanation());
        List<Movie> movieList = this.getMovieDocumentResult(searchHits);

        return movieList;

    }

    @Override
    public List<Movie> searchByUserHeartMovieAndCrew(List<Movie> heartedMovieList, Pageable pageable) {
        Set<String> crewNameSet = new HashSet<>();

        heartedMovieList.forEach(movie -> {
            movie.getMovieRCrewList().stream()
                    // 1) orderNo(또는 원하는 필드)에 따라 정렬
                    .sorted(Comparator.comparing(rc -> rc.getMovieCrew().getOrderNo()))
                    // 2) role이 "CAST"인 것만 필터링
                    .filter(rc -> "C".equals(rc.getMovieCrew().getRole().getValue()))
                    // 3) 그 중 상위 3개만
                    .limit(3)
                    // 4) crewNameSet에 영화인 이름 추가
                    .forEach(rc -> crewNameSet.add(rc.getMovieCrew().getName()));
        });

        List<Query> mustNotQueryList = new ArrayList<>();
        heartedMovieList.forEach(m -> {
            mustNotQueryList.add(Query.of(q -> q.term(t -> t.field("movieId").value(m.getMovieId()))));
        });

        // Nested Query 생성
        NestedQuery nestedQuery = NestedQuery.of(n -> n
                .path("movieCrew")
                .query(q -> q.bool(b -> b
                                .should(crewNameSet.stream()
                                        .map(name -> Query.of(query -> query.term(t -> t
                                                .field("movieCrew.name").value(name)
                                        )))
                                        .collect(Collectors.toList())
                                )
                        )
                )
        );

        BoolQuery topLevelBoolQuery = BoolQuery.of(b -> b
                // must: 위에서 만든 nestedQuery를 넣어준다
                .must(Query.of(q -> q.nested(nestedQuery)))
                // must_not: movieId 제외 조건들
                .mustNot(mustNotQueryList)
        );
        // FunctionScoreQuery: 각 장르에 대해 가중치 설정
        List<FunctionScore> functions = new ArrayList<>();

        crewNameSet.forEach(name ->
                functions.add(FunctionScore.of(f -> f
                        .filter(NestedQuery.of(n -> n
                                .path("movieCrew")
                                .query(q -> q.term(t -> t
                                        .field("movieCrew.name")
                                        .value(name)
                                ))
                        )._toQuery())
                        .weight(1.5))
                )
        );

        FunctionScoreQuery functionScoreQuery = FunctionScoreQuery.of(f -> f
                .query(topLevelBoolQuery._toQuery())
                .functions(functions)  // 가중치 설정 함수
                .scoreMode(FunctionScoreMode.Sum)
                .boostMode(FunctionBoostMode.Sum)
        );

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(functionScoreQuery._toQuery())
                .withPageable(pageable)
                .withSort(Sort.by(
                        Sort.Order.desc("_score")
                ))
                .build();
        nativeQuery.setExplain(true);

        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);

//        log.info("Explain output: {}", searchHits.getSearchHits().get(0).getExplanation());
        List<Movie> movieList = this.getMovieDocumentResult(searchHits);

        return movieList;
    }

    @Override
    public List<MovieDocument> searchMovieList(String inputStr, Pageable pageable) {
        // 영화 검색 API (should(term) 쿼리 : 현재는 제목, 배우 이름, 장르로 검색가능)
        log.info("검색 시작 : {}", inputStr);
        Query query = Query.of(q -> q
                .bool(b -> b
                        .should(Query.of(q1 -> q1.match(m -> m.field("title").query(inputStr)
                                        .boost(1.8f))),
                                Query.of(q1 -> q1.match(t -> t.field("title.en").query(inputStr)
                                        .boost(1.8f))),
                                Query.of(q1 -> q1.match(t -> t.field("title.ngram").query(inputStr)
                                        .boost(1.8f))),
                                Query.of(fq -> fq.fuzzy(t -> t.field("title.standard").value(inputStr)
                                        .fuzziness("AUTO").boost(1.4f))),
                                Query.of(q2 -> q2.nested(n -> n
                                        .path("movieGenre").query(nq -> nq
                                                .term(t -> t.field("movieGenre.genreName").value(inputStr)
                                                        .boost(1.8f))
                                        )
                                )),
                                Query.of(q2 -> q2.nested(n -> n
                                        .path("movieGenre").query(nq -> nq
                                                .match(t -> t.field("movieGenre.genreName.ko").query(inputStr)
                                                        .boost(1.8f))
                                        )
                                )),
                                Query.of(q2 -> q2.nested(n -> n
                                        .path("movieGenre").query(fq -> fq
                                                .fuzzy(t -> t.field("movieGenre.genreName.standard").value(inputStr)
                                                        .fuzziness("AUTO").boost(1.4f))
                                        )
                                )),
                                Query.of(q3 -> q3.nested(n -> n
                                        .path("movieCrew").query(nq -> nq
                                                .match(t -> t.field("movieCrew.name.ko").query(inputStr)
                                                        .boost(1.8f))
                                        )
                                )),
                                Query.of(q3 -> q3.nested(n -> n
                                        .path("movieCrew").query(nq -> nq
                                                .match(t -> t.field("movieCrew.name.en").query(inputStr)
                                                        .boost(1.8f))
                                        )
                                )),
                                Query.of(q3 -> q3.nested(n -> n
                                        .path("movieCrew").query(nq -> nq
                                                .match(t -> t.field("movieCrew.name.ngram").query(inputStr)
                                                        .boost(1.8f))
                                        )
                                )),
                                Query.of(q3 -> q3.nested(n -> n
                                        .path("movieCrew").query(fq -> fq
                                                .fuzzy(t -> t.field("movieCrew.name.ngram").value(inputStr)
                                                        .fuzziness("AUTO").boost(1.4f))
                                        )
                                ))
                        )
                )
        );

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(query)
                .withPageable(pageable)
                .withSort(Sort.by(
                        Sort.Order.desc("_score")
                ))
                .build();
        nativeQuery.setExplain(true);

        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);

        List<MovieDocument> result = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        return result;

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
