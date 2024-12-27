package movlit.be.book.detail.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.detail.domain.Book;
import movlit.be.book.detail.domain.repository.BookDetailRepository;
import movlit.be.book.testBook.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDetailReadService {

    private BookDetailRepository bookDetailRepository;

    public Book findByBookId(String bookId){

        return bookDetailRepository.findByBookId(bookId);
    }


}
