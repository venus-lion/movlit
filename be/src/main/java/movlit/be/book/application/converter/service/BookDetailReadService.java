package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.common.util.ids.BookId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDetailReadService {
    private final BookRepository bookRepository;

    public Book findByBookId(BookId bookId) {
        return bookRepository.findByBookId(bookId);
    }


}
