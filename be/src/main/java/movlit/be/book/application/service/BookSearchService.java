package movlit.be.book.application.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionBoostMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreMode;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.presentation.dto.BookRecommendDto;
import movlit.be.book.presentation.dto.BooksSearchResponseDto;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESVo;
import movlit.be.common.util.Genre;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    // 유저 취향 장르 기반, 추천 검색
    public List<BookRecommendDto> searchBooksByGenres(List<Genre> genres) {
        // 1. 장르 이름 추출
        List<String> genreNames = genres.stream()
                .map(Genre::getName)
                .toList();

        // 2. elasticsearch 쿼리 - 사용자 취향 장르와 일치하는 장르가 있다면, 가중치를 부여해서 높은 점수 순으로 가져오기

        // FunctionScoreQuery - 각 장르에 대한 가중치 설정
        List<FunctionScore> functions = new ArrayList<>();

        for (String genreName : genreNames) {
            functions.add(FunctionScore.of(f -> f
                    .filter(Query.of(q -> q
                            .match(m -> m.field("categoryName").query(genreName))))
                    .weight(2.0)) // 가중치 설정
            );
        }

        // bool 쿼리 생성
        Query shouldMatchCategoryNameQuery = Query.of(q -> q
                .bool(b -> b
                        .should(
                                MatchQuery.of(mq -> mq
                                        .field("categoryName")
                                        .query(String.join(" ", genreNames))
                                        .boost(1.0f)
                                )._toQuery()
                        )
                        .minimumShouldMatch("1")
                )
        );

        Query functionScoreQuery = FunctionScoreQuery.of(f -> f
                .query(shouldMatchCategoryNameQuery)
                .functions(functions)  // 가중치 설정 함수
                .scoreMode(FunctionScoreMode.Sum) // 점수 합산
                .boostMode(FunctionBoostMode.Multiply) // 부스트 곱셈
        )._toQuery();

        // NativeQuery 빌드 -- 최종 쿼리
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(functionScoreQuery)
                .withPageable(PageRequest.of(0, 30))
                .build();

        // 검색 실행
        SearchHits<BookES> searchHits = elasticsearchOperations.search(nativeQuery, BookES.class);

        // 값이 없다면..
        if (!searchHits.hasSearchHits()) {
            System.out.println("사용자 취향에 맞는 도서가 없습니다. -- /interestGenre");
            return null;
        }

        List<BookES> bookESList = searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        List<BookRecommendDto> bookRecommendDtos = bookESList.stream()
                .map(bookES -> BookESConvertor.documentToRecommendDto(bookES))
                .collect(Collectors.toList());

        return bookRecommendDtos;
    }

    // 사용자 최근 찜한 도서 추천
    public List<BookRecommendDto> fetchRecommendedBooksByUserRecentHeart(List<String> bookIds, List<BookES> bookESList,
                                                                         Pageable pageable) {
        /*
            사용자가 찜한 도서 '4'개 가져오기
            - description -> titleKeyword의 유사도(fuzziness)
            - category 필터링 -> categoryName, boost : 1.5
            - crew.keyword -> 정확한 매칭
            - 시리즈 제거
         */

        // 1. elasticsearch 쿼리 조건에 사용할 필드 필터링
        List<String> crewList = extractCrewList(bookESList);
        List<String> categoryList = extractCategoryList(bookESList);
        List<String> titleKeywordList = extractTitleKeywordList(bookESList);

        // 2. categoryName 필터링 -- 국내도서>소설/시/희곡>판타지/환상문학>한국판타지/환상소설 -> 가장 마지막 ">" 이후 부분을 추출하고 + '>'가 2개 이상 포함된 경우에만 필터링하기
        //  List<String> testCategory = new ArrayList<>();
        //  testCategory.add("국내도서>소설/시/희곡>판타지/환상문학>한국판타지/환상소설");
        //  testCategory.add("국내도서>만화>본격장르만화>판타지>드라마틱 판타지");
        //  testCategory.add("국내도서>소설/시/희곡>로맨스소설>한국 로맨스소설");
        //  testCategory.add("국내도서>소설/시/희곡>로맨스소설"); // 일부로 한 depths 뺀 예시 -- 최소한 depths가 '>'로 2개는 있고, 그때의 가장 마지막 '>' 다음  categoryName을 필터링할 것
        //  testCategory.add("국내도서>소설/시/희곡"); // 이때의 categoryName은 depth가 2라서 제외하고 싶음

        List<String> filteredCatgoryList = categoryList.stream()
                .filter(categoryName -> categoryName.split(">").length > 2) // ">"가 2개 이상인 경우만 필터링
                .map(categoryName -> {
                    String[] split = categoryName.split(">");
                    return split[split.length - 1].trim(); // 마지막 부분 추출 및 공백 제거
                })
                .collect(Collectors.toList());

        String joinCategory = String.join(" ", filteredCatgoryList);
        String joinTitleKeyword = String.join(" ", titleKeywordList);

        // 3. elasticsearch 쿼리
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 첫 번째 MatchQuery: description에 대한 쿼리와 boost 3.0 설정
        Query titleKeywordMatchQueryForShould = MatchQuery.of(t -> t
                .field("titleKeyword")
                .query(joinTitleKeyword)
                //  .boost(2.0f) // boost 값 추가
                .fuzziness("AUTO")
        )._toQuery();

        // 두 번째 MatchQuery: categoryName에 대한 쿼리와 boost 1.5 설정
        Query categoryNameMatchQuery = MatchQuery.of(t -> t
                .field("categoryName")
                .query(joinCategory)
                .boost(1.5f) // boost 값 추가
        )._toQuery();

        // 세 번째 termQuery : crew.keyword 매치
        Query crewKeywordTermQuery = Query.of(q -> q
                .terms(t -> t
                        .field("crew.keyword")
                        .terms(v -> v.value(
                                crewList.stream().map(crew -> FieldValue.of(crew)).collect(Collectors.toList()))
                        )
                )
        );

        // 제외할 조건 (must_not 절): 이미 찜한 도서는 제외
        // TermQuery: 해당 book은 제거
        Query excludeBookIdsQuery = Query.of(q -> q
                .terms(t -> t
                        .field("bookId") // 필드 이름
                        .terms(v -> v.value(bookIds.stream().map(id -> FieldValue.of(id)).collect(Collectors.toList()))
                        )
                )
        );

        // must_not -> match -> titleKeyword
        Query titleKeywordMatchQuery = MatchQuery.of(t -> t
                .field("titleKeyword")
                .query(joinTitleKeyword)
        )._toQuery();

        // BoolQuery에 MatchQuery 추가
        boolQueryBuilder
                .should(titleKeywordMatchQueryForShould)
                .should(categoryNameMatchQuery)
                .should(crewKeywordTermQuery)
                .mustNot(excludeBookIdsQuery)
                .mustNot(titleKeywordMatchQuery)
                .minimumShouldMatch("1");

        // BoolQuery 빌드
        BoolQuery boolQuery = boolQueryBuilder.build();

        // NativeSearchQuery 빌드 -- 최종 쿼리
        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(boolQuery._toQuery())
                .withPageable(pageable)
                .build();

        // 검색 실행
        SearchHits<BookES> searchHits = elasticsearchOperations.search(nativeQuery, BookES.class);

        // 값이 없을경우
        if (!searchHits.hasSearchHits()) {
            System.out.println("사용자 취향에 맞는 도서가 없습니다. -- /recommendations");
            return null; // 사용자 취향 리스트가 없음
        }

        List<BookES> bookESListForReturn = searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        System.out.println("bookESListForReturn ::: " + bookESListForReturn);

        List<BookRecommendDto> bookRecommendDtos = bookESListForReturn.stream()
                .map(bookES -> BookESConvertor.documentToRecommendDto(bookES))
                .collect(Collectors.toList());

        return bookRecommendDtos;
    }

    private List<String> extractCrewList(List<BookES> bookESList) {
        return bookESList.stream()
                .map(BookES::getCrew)
                .flatMap(List::stream)
                .toList();
    }

    private List<String> extractCategoryList(List<BookES> bookESList) {
        return bookESList.stream()
                .map(BookES::getCategoryName)
                .toList();
    }

    private List<String> extractTitleKeywordList(List<BookES> bookESList) {
        return bookESList.stream()
                .map(BookES::getTitleKeyword)
                .toList();
    }

    public BooksSearchResponseDto fetchSearchBook(String inputStr, int page, int pageSize) {
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page - 1);

        System.out.println("검색 시작 :::: " + inputStr);

        Query boolQuery = Query.of(q -> q
                .bool(bq -> bq.
                        should(
                                // 1. title
                                Query.of(q1 -> q1.match(m -> m.field("title").query(inputStr).boost(1.8F))),
                                Query.of(q1 -> q1.match(m -> m.field("title.ngram").query(inputStr).boost(1.8F))),
                                Query.of(fq -> fq.fuzzy(q1 -> q1.field("title.standard").value(inputStr)
                                        .fuzziness("AUTO").boost(1.4F))),
                                // 2. categoryName
                                Query.of(tq -> tq.term(q2 -> q2.field("categoryName").value(inputStr).boost(2.0F))),
                                Query.of(fq2 -> fq2.fuzzy(q3 -> q3.field("categoryName").value(inputStr)
                                        .fuzziness("AUTO").boost(2.0F))),
                                // 3. crew
                                Query.of(mq -> mq.match(q4 -> q4.field("crew").query(inputStr).boost(1.8F))),
                                Query.of(mq -> mq.match(q5 -> q5.field("crew.ngram").query(inputStr).boost(1.8F)))
                        )));

        NativeQuery nativeQuery = new NativeQueryBuilder()
                .withQuery(boolQuery)
                .withPageable(pageable)
                .withSort(Sort.by(
                        Order.desc("_score")
                ))
                .build();

        SearchHits<BookES> searchHits = elasticsearchOperations.search(nativeQuery, BookES.class);

        List<BookES> bookESList = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        for (BookES bookES : bookESList) {
            System.out.println("그때마다의 bookES의 categoryName :: " + bookES.getCategoryName());
        }

        // Total Page 구하기
        int pageSize2 = pageable.getPageSize();
        long totalHits = searchHits.getTotalHits();
        long totalPages = totalHits / pageSize2;

        if (totalHits % pageSize2 != 0) { // 데이터가 완전히 나눠지지 않아, 한 페이지 추가
            totalPages++;
        }

        List<BookESVo> bookESVos = bookESList.stream()
                .map(bookES -> BookESConvertor.documentToDomain(bookES))
                .collect(Collectors.toList());

        return BooksSearchResponseDto.builder()
                .bookESVoList(bookESVos)
                .totalPages(totalPages)
                .build();
    }

}
