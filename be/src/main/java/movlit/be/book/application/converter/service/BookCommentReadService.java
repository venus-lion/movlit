package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.dto.BookCommentsResponseDto;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentReadService {
    public static final int PAGE_SIZE = 10;


    private final BookCommentRepository bookCommentRepository;
    private final BookCommentLikeRepository bookCommentLikeRepository;


//    public Slice<BookCommentsResponseDto> getPagedBookComments(BookId bookId, Pageable pageable) {
//        Slice<BookCommentsResponseDto> bookCommentPage = bookCommentRepository.findByBookId(bookId, pageable);
//
//        return bookCommentPage;
//    }

    // 내가 작성한 도서 리뷰 찾기
    public BookComment findByMemberAndBook(Member member, Book book) {
        return bookCommentRepository.findByMemberAndBook(member, book);

    }

    public BookComment findByBookCommentId(BookCommentId bookCommentId) {
        return bookCommentRepository.findByBookCommentId(bookCommentId);
    }

    public BookCommentLike myCommentLikeStatus(Member member, Book book, BookComment bookComment){
        return bookCommentLikeRepository.findByBookCommentAndMember(bookComment, member);

    }

}
