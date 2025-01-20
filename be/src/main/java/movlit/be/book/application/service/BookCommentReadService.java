package movlit.be.book.application.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookCommentReadService {

    public static final int PAGE_SIZE = 10;

    private final BookCommentRepository bookCommentRepository;
//    private final BookCommentLikeRepository bookCommentLikeRepository;
//    private final BookCommentLikeCountRepository bookCommentLikeCountRepository;



    // 리뷰 리스트
    public Slice<BookCommentResponseDto> fetchPagedBookComments(BookId bookId, MemberId memberId, Pageable pageable) {
        Slice<BookCommentResponseDto> bookCommentPage = null;
        if (memberId == null) {
            bookCommentPage = bookCommentRepository.fetchByBookId(bookId, pageable);
        } else {
            bookCommentPage = bookCommentRepository.fetchByBookIdAndMemberId(bookId, memberId, pageable);
        }

        log.info("::BookCommentReadService_fetchPagedBookComments::");
        log.info(">> pagedBookComments : " + bookCommentPage.getContent());

        return bookCommentPage;
    }

    // 내가 작성한 도서 리뷰 찾기
    public BookCommentResponseDto fetchCommentByMemberAndBook(MemberId memberId, BookId bookId) {
        return bookCommentRepository.fetchCommentByMemberAndBook(memberId, bookId);

    }

    public BookCommentVo fetchByBookCommentId(BookCommentId bookCommentId) {
        return bookCommentRepository.fetchByBookCommentId(bookCommentId);
    }

//    // 댓글 좋아요 갯수
//    public int countLikesByCommentId(BookCommentId bookCommentId) {
//        return bookCommentLikeCountRepository.countLikeByCommentId(bookCommentId);
//    }
//
//    // 해당 댓글 나의 좋아요 여부
//    public boolean isLikedByComment(BookCommentVo bookCommentVo, Member member) {
//        if (bookCommentLikeRepository.fetchByBookCommentAndMember(bookCommentVo, member) != null) {
//            return true;
//        } else {
//            return false;
//        }
//    }



}
