package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.common.util.ids.BookId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDetailReadService {
    private final BookRepository bookRepository;
    private final BookHeartRepository bookHeartRepository;

    public Book findByBookId(BookId bookId) {
        return bookRepository.findByBookId(bookId);
    }



    public long countHeartsByBookId(BookId bookId){
        return bookHeartRepository.countHeartsByBookId(bookId);
    }


}
