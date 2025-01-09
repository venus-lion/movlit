package movlit.be.book.application.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.presentation.dto.BookDetailResponseDto;
import movlit.be.book.domain.Bookcrew;
import movlit.be.book.domain.repository.BookHeartCountRepository;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.domain.repository.BookcrewRepository;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESDomain;
import movlit.be.bookES.BookESRepository;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
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
public class BookDetailReadService {

    private final BookRepository bookRepository;
    private final BookcrewRepository bookcrewRepository;
    private final BookCommentRepository bookCommentRepository;
    private final BookHeartRepository bookHeartRepository;
    private final BookHeartCountRepository bookHeartCountRepository;

    // Elasticsearch - 관련 도서 추천
    private final BookESRepository bookESRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    // 도서 상세 정보
    public BookDetailResponseDto getBookDetail(BookId bookId, Member member) {
        Book book = findByBookId(bookId);
        // 평점 - 소수 둘째자리까지
        double averageScore = Math.round(getAverageScoreByBookId(bookId)*100)/100.0;
        int heartCount = countHeartsByBookId(bookId);
        boolean isHearted = false;
        if(member != null)
            isHearted = isHeartedByBook(book, member);
        List<Bookcrew> bookcrewList = findBookcrewByBook(book);
        BookDetailResponseDto bookDetailResponse = BookDetailResponseDto.builder()
                .bookId(book.getBookId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .publisher(book.getPublisher())
                .pubDate(book.getPubDate())
                .description(book.getDescription())
                .categoryName(book.getCategoryName())
                .bookImgUrl(book.getBookImgUrl())
                .stockStatus(book.getStockStatus())
                .mallUrl(book.getMallUrl())
                .averageScore(BigDecimal.valueOf(averageScore))
                .heartCount(heartCount)
                .isHearted(isHearted)
                .bookcrewList(bookcrewList)
                .build();

        return bookDetailResponse;
    }

    public Book findByBookId(BookId bookId) {
        return bookRepository.findByBookId(bookId);
    }

    // 해당 책의 크루
    public List<Bookcrew> findBookcrewByBook(Book book) {
        return bookcrewRepository.findByBook(book);
    }

    // 해당 책의 평점
    public double getAverageScoreByBookId(BookId bookId){
        return bookCommentRepository.getAverageScoreByBookId(bookId);
    }

    // 찜 갯수
    public int countHeartsByBookId(BookId bookId) {
        return bookHeartCountRepository.countHeartByBookId(bookId);
    }

    // 해당 책 나의 찜 여부
    public boolean isHeartedByBook(Book book, Member member){
         if(bookHeartRepository.findByBookAndMember(book, member) != null)
             return true;
         else
             return false;
    }

    // 관련 도서 추천
    public List<BookESDomain> getRecommendedBooks(BookId bookId) {
        Pageable pageable = PageRequest.of(0, 10);
        BookES bookES = bookESRepository.findById(bookId.getValue()).orElse(null);
        System.out.println("## bookId : " + bookId.getValue() + "\n## bookES : " + bookES);
        if (bookES != null) {
            String category = bookES.getCategoryName();
            String titleKeyword = bookES.getTitleKeyword();
            String description = bookES.getDescription();
            System.out.println("%% 해당 책의 category : " + category + "\n%% 해당 책의 titleKeyword : " + titleKeyword);

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

            List<BookESDomain> bookESDomainList = bookESListForReturn.stream()
                    .map(resultbookES -> BookESConvertor.documentToDomain(resultbookES))
                    .collect(Collectors.toList());

            return bookESDomainList;

        } else {
            return null;
        }


    }


    // 관련 영화 추천
    public List<MovieDocument> getRecommendedMovies(BookId bookId) {
        Pageable pageable = PageRequest.of(0, 10);
        BookES bookES = bookESRepository.findById(bookId.getValue()).orElse(null);
        MovieDocument movieDocument = null;
        System.out.println("## bookId : " + bookId.getValue() + "\n## bookES : " + bookES);
        if (bookES != null) {
            String category = bookES.getCategoryName();
            String titleKeyword = bookES.getTitleKeyword();
            String description = bookES.getDescription();
            System.out.println("%% 해당 책의 category : " + category + "\n%% 해당 책의 titleKeyword : " + titleKeyword);

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
