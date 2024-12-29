package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.repository.BookCommentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentWriteService {

    private final BookCommentRepository bookCommentRepository;

    public BookComment registerBookComment(BookComment bookComment) {
        return bookCommentRepository.save(bookComment);
    }

}