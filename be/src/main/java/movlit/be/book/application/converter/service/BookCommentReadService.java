package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentReadService {
    public static final int PAGE_SIZE = 10;


    private final BookCommentRepository bookCommentRepository;
    private final BookCommentLikeRepository bookCommentLikeRepository;

//    public Page<BookComment> getPagedBookComments(BookId bookId, int page) {
//        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
//        Page<BookComment> bookCommentPage = bookCommentRepository.findByBookId(bookId, pageable);
//
//        return bookCommentPage;
//    }

    public BookComment findByBookCommentId(BookCommentId bookCommentId) {
        return bookCommentRepository.findByBookCommentId(bookCommentId);
    }

    public BookCommentLike myCommentLikeStatus(Member member, Book book, BookComment bookComment){
        return bookCommentLikeRepository.findByBookCommentAndMember(bookComment, member);

    }

}
