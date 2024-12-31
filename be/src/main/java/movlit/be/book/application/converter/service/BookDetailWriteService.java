package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookDetailWriteService {

    private final BookRepository bookRepository;
    private final BookHeartRepository bookHeartRepository;

    public BookHeart addHeart(Member member, Book book) {
        BookHeart existingHeart = bookHeartRepository.findByBookAndMember(book, member);
        if (existingHeart == null) {
            BookHeart bookHeart = BookHeart.builder()
                    .book(book)
                    .member(member)
                    .build();
            return bookHeartRepository.save(bookHeart);
        } else {
            return existingHeart;
        }
    }

    public void removeHeart(Member member, Book book) throws Exception {
        BookHeart existingHeart = bookHeartRepository.findByBookAndMember(book, member);
        if (existingHeart != null)
            bookHeartRepository.delete(existingHeart);
        else
            throw new Exception("찜하기를 삭제할 수 없습니다");
    }


}
