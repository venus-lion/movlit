package movlit.be.book.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.repository.BookCommentLikeCountRepository;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentReadService {

    public static final int PAGE_SIZE = 10;

    private final BookCommentRepository bookCommentRepository;
    private final BookCommentLikeRepository bookCommentLikeRepository;
    private final BookCommentLikeCountRepository bookCommentLikeCountRepository;



    // 리뷰 리스트
    public Slice<BookCommentResponseDto> fetchPagedBookComments(BookId bookId, MemberId memberId, Pageable pageable) {
        Slice<BookCommentResponseDto> bookCommentPage = null;
        if (memberId == null) {
            bookCommentPage = bookCommentRepository.fetchByBookId(bookId, pageable);
        } else {
            bookCommentPage = bookCommentRepository.fetchByBookIdAndMemberId(bookId, memberId, pageable);
        }

        System.out.println("$$$$$$$ 리뷰리스트 : ");
        for (BookCommentResponseDto comment : bookCommentPage.getContent()) {
            // 각 BookCommentResponseDto의 내용 출력
            System.out.println("Comment TEXT: " + comment.getComment());
            System.out.println("Comment LIKED: " + comment.isLiked());
            System.out.println("COMMENTS NUM" + comment.getAllCommentsCount());
            // 필요에 따라 추가 속성을 출력할 수 있습니다.
        }

        return bookCommentPage;
    }

    // 내가 작성한 도서 리뷰 찾기
    public BookCommentVo fetchByMemberAndBook(Member member, BookVo bookVo) {
        return bookCommentRepository.fetchByMemberAndBook(member, bookVo);

    }

    public BookCommentVo fetchByBookCommentId(BookCommentId bookCommentId) {
        return bookCommentRepository.fetchByBookCommentId(bookCommentId);
    }

    // 댓글 좋아요 갯수
    public int countLikesByCommentId(BookCommentId bookCommentId) {
        return bookCommentLikeCountRepository.countLikeByCommentId(bookCommentId);
    }

    // 해당 댓글 나의 좋아요 여부
    public boolean isLikedByComment(BookCommentVo bookCommentVo, Member member) {
        if (bookCommentLikeRepository.fetchByBookCommentAndMember(bookCommentVo, member) != null) {
            return true;
        } else {
            return false;
        }
    }



}
