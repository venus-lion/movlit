package movlit.be.book.application.service;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.infra.persistence.recommend_jpa.BookHeartRecommendRepository;
import movlit.be.common.util.ids.MemberId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BooksRecommendationService {
    private final BookHeartRecommendRepository bookHeartRecommendRepository;

    // dto 수정 필요
    public List<Book> getRecommendedBook(MemberId memberId){
        // 1. 사용자가 최근에 찜한 도서id 5개 가져오기
        List<String> bookIds = bookHeartRecommendRepository.findRecentLikedBookIdsByMemberId(
                memberId, 5);

        // 2. elastic 쿼리로, 사용자 맞춤 추천 날리기
            // -- categoryName 값은 항상 있는데, description은 없을 수도 있어서 가중치를 categoryName에 더 크게 두자

        return null;

    }

//    @Builder
//    @Getter
//    private class GetBookIdDescriptionAndCategory {
//        private String bookId;
//    }
}
