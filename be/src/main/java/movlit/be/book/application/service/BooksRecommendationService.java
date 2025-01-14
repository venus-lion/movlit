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
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import movlit.be.book.infra.persistence.recommend_jpa.BookHeartRecommendRepository;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESDomain;
import movlit.be.bookES.BookESRepository;
import movlit.be.common.exception.MemberGenreNotFoundException;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BooksRecommendationService {
    private final ElasticsearchOperations elasticsearchOperations;
    private final BookHeartRecommendRepository bookHeartRecommendRepository;
    private final BookESRepository bookESRepository;
    private final MemberGenreRepository memberGenreRepository;

    public List<BookESDomain> getRecommendedBook(MemberId memberId){
        Pageable pageable = PageRequest.of(0, 30);

        // 1. 사용자가 최근에 찜한 도서id 4개 가져오기
        List<String> bookIds = bookHeartRecommendRepository.findRecentLikedBookIdsByMemberId(
                memberId, 4);

        // 2. JPA를 사용해, 도서 정보 조회 -> BookES Repository 사용
        Iterable<BookES> bookESIterable = bookESRepository.findAllById(bookIds);
        List<BookES> bookESList = StreamSupport.stream(bookESIterable.spliterator(), false)
                .collect(Collectors.toList());


        /*
            TODO :: 개선
                -> 사용자가 찜한 도서 '4'개 가져오기
                -> description -> titleKeyword의 유사도(fuzziness)
                -> category 필터링 -> categoryName, boost : 1.5
                -> crew.keyword -> 정확한 매칭
                -> 시리즈 제거
         */

        List<String> crewList = bookESList.stream()
                .map(BookES::getCrew)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        for (String crew : crewList){
            System.out.println("crew :: " + crew);
        }

        List<String> categoryList = bookESList.stream()
                .map(book -> book.getCategoryName())
                .collect(Collectors.toList());

        List<String> titleKeywordList = bookESList.stream()
                .map(book -> book.getTitleKeyword())
                .collect(Collectors.toList());

        // 2-2. categoryName 필터링 -- 국내도서>소설/시/희곡>판타지/환상문학>한국판타지/환상소설 -> 가장 마지막 ">" 이후 부분을 추출하고 + '>'가 2개 이상 포함된 경우에만 필터링하기
//        List<String> testCategory = new ArrayList<>();
//        testCategory.add("국내도서>소설/시/희곡>판타지/환상문학>한국판타지/환상소설");
//        testCategory.add("국내도서>만화>본격장르만화>판타지>드라마틱 판타지");
//        testCategory.add("국내도서>소설/시/희곡>로맨스소설>한국 로맨스소설");
//        testCategory.add("국내도서>소설/시/희곡>로맨스소설"); // 일부로 한 depths 뺀 예시 -- 최소한 depths가 '>'로 2개는 있고, 그때의 가장 마지막 '>' 다음  categoryName을 필터링할 것
//        testCategory.add("국내도서>소설/시/희곡"); // 이때의 categoryName은 depth가 2라서 제외하고 싶음

        List<String> filteredCatgoryList = categoryList.stream()
                .filter(categoryName -> categoryName.split(">").length > 2) // ">"가 2개 이상인 경우만 필터링
                .map(categoryName -> {
                    String[] split = categoryName.split(">");
                    return split[split.length - 1].trim(); // 마지막 부분 추출 및 공백 제거
                })
                .collect(Collectors.toList());

        String joinCategory = String.join(" ", filteredCatgoryList);
        System.out.println("JoinCategory :: " + joinCategory);
        String joinTitleKeyword = String.join(" ", titleKeywordList);
        System.out.println("JoinTitleKeyword :: " + joinTitleKeyword);

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
//                .withSort(Sort.by(
//                        Sort.Order.desc("_score")
////                        Sort.Order.desc("voteAverage")      // score순, 평점 순
//                ))
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

        List<BookESDomain> bookESDomainList = bookESListForReturn.stream()
                .map(bookES -> BookESConvertor.documentToDomain(bookES))
                .collect(Collectors.toList());

        return bookESDomainList;



    }

    public List<BookESDomain> getBookUserInterestByGenre(MemberId memberId){
        List<Genre> bookGenreList = memberGenreRepository.findUserInterestGenreList(memberId);

        if (bookGenreList.isEmpty()){
            throw new MemberGenreNotFoundException();
        }

        // 사용자가 선택한 장르 이름을 추출해, 쿼리 생성에 사용
        List<String> genreNameList = bookGenreList.stream()
                .map(genre -> genre.getName())
                .collect(Collectors.toList());

        for (String genreName : genreNameList){
            System.out.println("genreName ::: " + genreName);
        }

        // elasticsearch 쿼리 - 사용자 취향 장르와 일치하는 장르가 있다면, 가중치를 부여해서 높은 점수 순으로 가져오기

        // FunctionScoreQuery - 각 장르에 대한 가중치 설정
        List<FunctionScore> functions = new ArrayList<>();

        for (String genreName : genreNameList){
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
                                        .query(String.join(" ", genreNameList))
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
        if (!searchHits.hasSearchHits()){
            System.out.println("사용자 취향에 맞는 도서가 없습니다. -- /interestGenre");
            return null;
        }

        List<BookES> bookESList = searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        List<BookESDomain> bookESDomainList = bookESList.stream()
                .map(bookES -> BookESConvertor.documentToDomain(bookES))
                .collect(Collectors.toList());

        return bookESDomainList;
    }
}
