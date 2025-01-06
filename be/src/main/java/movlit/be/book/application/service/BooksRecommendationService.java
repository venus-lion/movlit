package movlit.be.book.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.infra.persistence.recommend_jpa.BookHeartRecommendRepository;
import movlit.be.bookES.BookES;
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

    // dto 수정 필요
    public List<BookES> getRecommendedBook(MemberId memberId){
        Pageable pageable = PageRequest.of(0, 10);

        // 1. 사용자가 최근에 찜한 도서id 5개 가져오기
        List<String> bookIds = bookHeartRecommendRepository.findRecentLikedBookIdsByMemberId(
                memberId, 5);

        for (String bookId : bookIds){
            System.out.println("bookID :::: " +bookId);
        }

        // 2. Elasticsearch에서 찜한 도서 정보 가져오기
        List<Book> bookList = new ArrayList<>();
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

        List<String> book = new ArrayList<>();

        // 첫 번째 MatchQuery: description에 대한 쿼리와 boost 1.5 설정
        Query descriptionMatchQuery = MatchQuery.of(t -> t
                .field("description")
                .query("군주들의 일격에 목숨을 잃는 성진우. 그러나 검은 심장의 힘으로 다시 눈을 뜨고, 죽음의 끝에서 그림자 군주 '아스본'을 만나게 된다. 평온한 세계에서의 안식과 군주들과의 잔혹한 전쟁, 선택의 갈림길에 선 성진우의 선택은…?!")
                .boost(1.5f) // boost 값 추가
        )._toQuery();

        // 두 번째 MatchQuery: categoryName에 대한 쿼리와 boost 2.0 설정
        Query categoryNameMatchQuery = MatchQuery.of(t -> t
                .field("categoryName")
                .query("드라마틱 판타지")
                .boost(3.0f) // boost 값 추가
        )._toQuery();

        // 제외할 조건 (must_not 절): 이미 찜한 도서는 제외
        // TermQuery: 해당 book은 제거
        Query excludeMovieIdQuery = TermQuery.of(t -> t
                .field("bookId")
                .value("9791193821398") // 해당 movie는 제거 --> 일단 bookId
        )._toQuery();

        // BoolQuery에 MatchQuery 추가
        boolQueryBuilder
                .should(descriptionMatchQuery)
                .should(categoryNameMatchQuery)
                .mustNot(excludeMovieIdQuery)
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

        List<BookES> bookESList = searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        System.out.println("bookESList ::: " + bookESList);
//        bookList = searchHits.stream()
//                .map(hit -> MovieDocumentConverter.documentToEntity(hit.getContent()))
//                .collect(Collectors.toList());

        return bookESList;



    }

//    @Getter
//    @ToString
//    public class GetBookIdDescriptionAndCategory {
//        private String bookId;
//        private String description;
//        private String categoryName;
//    }
}
