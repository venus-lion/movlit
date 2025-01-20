package movlit.be.book.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.infra.persistence.recommend_jpa.BookHeartRecommendRepository;
import movlit.be.common.exception.BookHeartNotFoundException;
import movlit.be.common.util.ids.MemberId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookHeartReadService {
    private final BookHeartRecommendRepository bookHeartRecommendRepository;

    public List<String> fetchRecentLikedBookIdsByMemberId(MemberId memberId, int count){
        List<String> bookIds = bookHeartRecommendRepository.findRecentLikedBookIdsByMemberId(
                memberId, count);

        if (bookIds.isEmpty()){
            // 해당 유저가 찜한 도서가 존재하지 않는다.
            throw new BookHeartNotFoundException();
        }
        return bookIds;
    }
}
