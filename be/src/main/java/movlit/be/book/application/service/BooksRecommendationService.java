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
import movlit.be.book.presentation.dto.BookRecommendDto;
import movlit.be.bookES.BookES;
import movlit.be.bookES.BookESConvertor;
import movlit.be.bookES.BookESRepository;
import movlit.be.bookES.BookESService;
import movlit.be.common.util.Genre;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberGenreService;
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
    private final BookSearchService bookSearchService;
    private final MemberGenreService memberGenreService;
    private final BookHeartService bookHeartService;
    private final BookESService bookESService;


    public List<BookRecommendDto> fetchBookUserInterestByGenre(MemberId memberId){
        // 1. 유저의 취향장르 가져오기
        List<Genre> bookGenreList = memberGenreService.fetchUserInterestGenreList(memberId);

        // 2. 장르 기반 추천 검색 실행
        return bookSearchService.searchBooksByGenres(bookGenreList);
    }

    public List<BookRecommendDto> fetchRecommendedBooksByUserRecentHeart(MemberId memberId){
        // 1. 사용자가 최근에 찜한 도서id 4개 가져오기
        List<String> bookIds = bookHeartService.fetchRecentLikedBookIdsByMemberId(memberId, 4);

        // 2. JPA를 사용해, 도서 정보 조회 -> BookES Repository 사용
        List<BookES> bookESList = bookESService.fetchAllBookESByBookIds(bookIds);

        // 3. 최근 찜기반 도서 추천
        Pageable pageable = PageRequest.of(0, 30);

        return bookSearchService.fetchRecommendedBooksByUserRecentHeart(bookIds, bookESList, pageable);
    }

}
