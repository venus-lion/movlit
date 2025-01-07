package movlit.be.movie.infra.persistence;

import co.elastic.clients.elasticsearch._types.query_dsl.*;

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
import movlit.be.movie.domain.MovieRole;
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

        log.info("Explain output: {}", searchHits.getSearchHits().get(0).getExplanation());
        List<Movie> movieList = this.getMovieDocumentResult(searchHits);

        return movieList;

    }

    @Override
    public List<Movie> searchByUserHeartMovieAndCrew(List<Movie> heartedMovieList, Pageable pageable) {
        // TODO : 위와 같이 비슷한 로직(대표 crew 1명 뽑기)
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
        log.info("===crewNameSet : {}", crewNameSet);

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

        log.info("Explain output: {}", searchHits.getSearchHits().get(0).getExplanation());
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
