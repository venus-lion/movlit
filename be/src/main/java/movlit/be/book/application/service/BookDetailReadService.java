package movlit.be.book.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.presentation.dto.BookCrewResponseDto;
import movlit.be.book.presentation.dto.BookDetailResponseDto;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESRepository;
import movlit.be.bookES.BookESVo;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.movie.domain.document.MovieDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookDetailReadService {

    private final BookRepository bookRepository;

    // Elasticsearch - 관련 도서 추천
    private final BookESRepository bookESRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    // 도서 상세 정보 (리팩토링 후)
    public BookDetailResponseDto fetchBookDetail(BookId bookId, MemberId memberId) {

        // 1. 책 정보 조회
        Optional<BookDetailResponseDto> bookDetailsOpt = Optional.ofNullable(
                bookRepository.fetchBookDetailByBookId(bookId, memberId));

        // 2. 크루 정보 조회
        List<BookCrewResponseDto> crewList = bookRepository.fetchBookCrewByBookId(bookId);

        // 3. 결과 조합
        if (bookDetailsOpt.isPresent()) {
            BookDetailResponseDto bookDetails = bookDetailsOpt.get();
            bookDetails.setBookcrewList(crewList);
            return bookDetails;
        }

        throw new BookNotFoundException();  // 또는 예외 처리
    }

    public BookVo fetchByBookId(BookId bookId) {
        return bookRepository.fetchByBookId(bookId);
    }

//    // 해당 책의 크루
//    public List<BookcrewVo> fetchBookcrewByBook(BookVo bookVo) {
//        return bookcrewRepository.fetchByBook(bookVo);
//    }
//
//    // 해당 책의 평점
//    public double fetchAverageScoreByBookId(BookId bookId){
//        return bookCommentRepository.fetchAverageScoreByBookId(bookId);
//    }
//
//    // 찜 갯수
//    public int countHeartsByBookId(BookId bookId) {
//        return bookHeartCountRepository.countHeartByBookId(bookId);
//    }
//
//    // 해당 책 나의 찜 여부
//    public boolean isHeartedByBook(BookVo bookVo, Member member){
//         if(bookHeartRepository.fetchByBookAndMember(bookVo, member) != null)
//             return true;
//         else
//             return false;
//    }

    // 관련 도서 추천
    public List<BookESVo> fetchRecommendedBooks(BookId bookId) {
        Pageable pageable = PageRequest.of(0, 10);
        BookES bookES = bookESRepository.findById(bookId.getValue()).orElse(null);
        log.info("::BookDetailReadService_fetchRecommendedBooks::");

        if (bookES != null) {
            String category = bookES.getCategoryName();
            String titleKeyword = bookES.getTitleKeyword();
            String description = bookES.getDescription();
            System.out.println(">> 해당 책의 category : " + category + "\n>> 해당 책의 titleKeyword : " + titleKeyword);

            // 3. elasticsearch 쿼리
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

            // 첫 번째 MatchQuery: description에 대한 쿼리와 boost 3.0 설정
            Query titleKeywordMatchQueryForShould = MatchQuery.of(t -> t
                    .field("titleKeyword")
                    .query(titleKeyword)
                    //  .boost(2.0f) // boost 값 추가
                    .fuzziness("AUTO")
            )._toQuery();

            // 두 번째 MatchQuery: categoryName에 대한 쿼리와 boost 1.5 설정
            Query categoryNameMatchQuery = MatchQuery.of(t -> t
                    .field("categoryName")
                    .query(category)
                    .boost(1.5f) // boost 값 추가
            )._toQuery();

            Query DescriptionMatchQueryForShould = MatchQuery.of(t -> t
                    .field("description")
                    .query(description)
                    .boost(1.5f) // boost 값 추가
            )._toQuery();

            // BoolQuery에 MatchQuery 추가
            boolQueryBuilder
                    .should(titleKeywordMatchQueryForShould)
                    .should(categoryNameMatchQuery)
                    .should(DescriptionMatchQueryForShould)
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

            List<BookESVo> bookESVoList = bookESListForReturn.stream()
                    .map(resultbookES -> BookESConvertor.documentToDomain(resultbookES))
                    .collect(Collectors.toList());

            return bookESVoList;

        } else {
            return null;
        }


    }

    // 관련 영화 추천
    public List<MovieDocument> fetchRecommendedMovies(BookId bookId) {
        Pageable pageable = PageRequest.of(0, 10);
        BookES bookES = bookESRepository.findById(bookId.getValue()).orElse(null);
        MovieDocument movieDocument = null;
        log.info("::BookDetailReadService_fetchRecommendedBooks::");

        if (bookES != null) {
            String category = bookES.getCategoryName();
            String titleKeyword = bookES.getTitleKeyword();
            String description = bookES.getDescription();
            System.out.println(">> 해당 책의 category : " + category + "\n>> 해당 책의 titleKeyword : " + titleKeyword);

            // 3. elasticsearch 쿼리
            BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();

            // 첫 번째 MatchQuery: description에 대한 쿼리와 boost 3.0 설정
            Query titleKeywordMatchQueryForShould = MatchQuery.of(t -> t
                    .field("title")
                    .query(titleKeyword)
                    //  .boost(2.0f) // boost 값 추가
                    .fuzziness("AUTO")
            )._toQuery();

            // 두 번째 MatchQuery: categoryName에 대한 쿼리와 boost 1.5 설정
            Query categoryNameMatchQuery = MatchQuery.of(t -> t
                    .field("movieGenre")
                    .query(category)
                    .boost(1.5f) // boost 값 추가
            )._toQuery();

            Query DescriptionMatchQueryForShould = MatchQuery.of(t -> t
                    .field("overview")
                    .query(description)
                    .boost(1.5f) // boost 값 추가
            )._toQuery();

            // BoolQuery에 MatchQuery 추가
            boolQueryBuilder
                    .should(titleKeywordMatchQueryForShould)
                    .should(categoryNameMatchQuery)
                    .should(DescriptionMatchQueryForShould)
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
            SearchHits<MovieDocument> searchHits = elasticsearchOperations.search(nativeQuery, MovieDocument.class);

            // 값이 없을경우
            if (!searchHits.hasSearchHits()) {
                System.out.println("사용자 취향에 맞는 영화가 없습니다.");
                return null; // 사용자 취향 리스트가 없음
            }

            List<MovieDocument> MovieDocumentListForReturn = searchHits.getSearchHits().stream()
                    .map(hit -> hit.getContent())
                    .collect(Collectors.toList());

            System.out.println("MovieDocumentListForReturn ::: " + MovieDocumentListForReturn);

            List<MovieDocument> MovieDocumentList = MovieDocumentListForReturn.stream()
                    .collect(Collectors.toList());

            return MovieDocumentList;

        } else {
            return null;
        }


    }


}
