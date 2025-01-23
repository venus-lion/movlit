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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.util.Genre;
import movlit.be.movie.application.converter.main.MovieDocumentConverter;
import movlit.be.movie.domain.Movie;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.repository.MovieSearchRepository;
import movlit.be.movie.presentation.dto.response.MovieCrewResponseDto;
import movlit.be.movie.presentation.dto.response.MovieDocumentResponseDto;
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

    @Override
    public List<Movie> searchMovieByMemberInterestGenre(List<Genre> genreList, Pageable pageable) {
        // 사용자 취향 장르와 일치하는 장르가 있으면 가중치를 부여해서 점수 높은 순으로 가져오기

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
    public List<Movie> searchMovieByMemberHeartCrew(List<MovieCrewResponseDto> crewList, Pageable pageable) {

        Map<Long, Set<String>> groupedMap = crewList.stream()
                // role 값이 "c"인 항목만 필터링
                .filter(dto -> "C".equals(dto.role().getValue()))
                // movieId 기준으로 먼저 정렬하고, 그 다음 orderNo 기준으로 정렬
                .collect(Collectors.groupingBy(
                        MovieCrewResponseDto::movieId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream().sorted(Comparator.comparingInt(MovieCrewResponseDto::orderNo))
                                        .limit(3)
                                        .map(MovieCrewResponseDto::name)
                                        .collect(Collectors.toSet())
                        )
                ));

        Set<String> crewNameSet = groupedMap.values().stream()
                .flatMap(Set::stream).collect(Collectors.toSet());
        log.info("CrewNameSet: {}", crewNameSet);

        List<Query> mustNotQueryList = new ArrayList<>();
        crewList.forEach(c -> {
            mustNotQueryList.add(Query.of(q -> q.term(t -> t.field("movieId").value(c.movieId()))));
        });

        // Nested Query 생성
        NestedQuery nestedQuery = NestedQuery.of(n -> n
                .path("movieCrew")
                .query(q -> q.bool(b -> b
                                .should(crewNameSet.stream()
                                        .flatMap(name -> Stream.of(
                                                Query.of(query -> query.match(t -> t
                                                        .field("movieCrew.name.ko").query(name)
                                                )),
                                                Query.of(query -> query.match(t -> t
                                                        .field("movieCrew.name.en").query(name)
                                                ))
                                        ))
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

        crewNameSet.forEach(name -> {
                    functions.add(FunctionScore.of(f -> f
                            .filter(NestedQuery.of(n -> n
                                    .path("movieCrew")
                                    .query(q -> q.match(t -> t
                                            .field("movieCrew.name.ko")
                                            .query(name)
                                    ))
                            )._toQuery())
                            .weight(1.5))
                    );
                    functions.add(FunctionScore.of(f -> f
                            .filter(NestedQuery.of(n -> n
                                    .path("movieCrew")
                                    .query(q -> q.match(t -> t
                                            .field("movieCrew.name.en")
                                            .query(name)
                                    ))
                            )._toQuery())
                            .weight(1.5))
                    );
                }
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

        log.info("Explain output: {}", searchHits.getSearchHits());
        List<Movie> movieList = this.getMovieDocumentResult(searchHits);

        return movieList;
    }

    @Override
    public MovieDocumentResponseDto searchMovieList(String inputStr, Pageable pageable) {
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

        log.info("Executing query: {}", query);
        log.info("Executing nativeQuery: {}", nativeQuery);

        SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);

        List<MovieDocument> result = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        // Total Page 구하기
        int pageSize = pageable.getPageSize();
        long totalHits = searchHits.getTotalHits();
        long totalPages = totalHits / pageSize;
        if (totalHits % pageSize != 0) {
            totalPages++;
        }
        return new MovieDocumentResponseDto(result, totalPages);

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
