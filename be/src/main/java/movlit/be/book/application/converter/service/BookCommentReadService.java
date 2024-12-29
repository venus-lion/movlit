package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentReadService {
    public static final int PAGE_SIZE = 10;


    private final BookCommentRepository bookCommentRepository;

//    public Page<BookComment> getPagedBookComments(BookId bookId, int page) {
//        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
//        Page<BookComment> bookCommentPage = bookCommentRepository.findByBookId(bookId, pageable);
//
//        return bookCommentPage;
//    }

}
