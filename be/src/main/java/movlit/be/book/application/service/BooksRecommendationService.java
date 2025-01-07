package movlit.be.book.application.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.infra.persistence.recommend_jpa.BookHeartRecommendRepository;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESDomain;
import movlit.be.bookES.BookESRepository;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
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
    private final BookRepository bookRepository;
    private final BookESRepository bookESRepository;

    // dto 수정 필요
    public List<BookESDomain> getRecommendedBook(MemberId memberId){
        Pageable pageable = PageRequest.of(0, 10);

        // 1. 사용자가 최근에 찜한 도서id 5개 가져오기
        List<String> bookIds = bookHeartRecommendRepository.findRecentLikedBookIdsByMemberId(
                memberId, 5);

        // 2. JPA를 사용해, 도서 정보 조회 -> BookES Repository 사용
        Iterable<BookES> bookESIterable = bookESRepository.findAllById(bookIds);
        List<BookES> bookESList = StreamSupport.stream(bookESIterable.spliterator(), false)
                .collect(Collectors.toList());


        // 2-1. 도서의 description과 categoryName 추출
        List<String> descriptionList = bookESList.stream()
                .map(book -> book.getDescription())
                .collect(Collectors.toList());

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

        String joinDiscription = String.join(" ", descriptionList);
        System.out.println("joinDiscription ::: " + joinDiscription);
        String joinCategory = String.join(" ", filteredCatgoryList);
        System.out.println("JoinCategory :: " + joinCategory);
        String joinTitleKeyword = String.join(" ", titleKeywordList);
        System.out.println("JoinTitleKeyword :: " + joinTitleKeyword);

        // 3. (임시쿼리) elasticsearch 쿼리
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        // 첫 번째 MatchQuery: description에 대한 쿼리와 boost 1.5 설정
        Query descriptionMatchQuery = MatchQuery.of(t -> t
                .field("description")
                //.query("군주들의 일격에 목숨을 잃는 성진우. 그러나 검은 심장의 힘으로 다시 눈을 뜨고, 죽음의 끝에서 그림자 군주 '아스본'을 만나게 된다. 평온한 세계에서의 안식과 군주들과의 잔혹한 전쟁, 선택의 갈림길에 선 성진우의 선택은…?!")
                .query(joinDiscription)
                .boost(3.0f) // boost 값 추가
        )._toQuery();

        // 두 번째 MatchQuery: categoryName에 대한 쿼리와 boost 2.0 설정
        Query categoryNameMatchQuery = MatchQuery.of(t -> t
                .field("categoryName")
                .query(joinDiscription)
                .boost(1.5f) // boost 값 추가
        )._toQuery();

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
                .should(descriptionMatchQuery)
                .should(categoryNameMatchQuery)
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
            System.out.println("사용자 취향에 맞는 도서가 없습니다.");
            return null; // 사용자 취향 리스트가 없음
        }

        List<BookES> bookESListForReturn = searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        System.out.println("bookESListForReturn ::: " + bookESListForReturn);
//
//        List<BookESDomain> bookESDomainList = bookESList.stream()
//                .map(bookES -> BookESConvertor.documentToDomain(bookES))
//                .collect(Collectors.toList());

        List<BookESDomain> bookESDomainList = bookESListForReturn.stream()
                .map(bookES -> BookESConvertor.documentToDomain(bookES))
                .collect(Collectors.toList());

        return bookESDomainList;



    }

//    @Getter
//    @ToString
//    public class GetBookIdDescriptionAndCategory {
//        private String bookId;
//        private String description;
//        private String categoryName;
//    }
}
