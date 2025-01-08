package movlit.be.book.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.BookHeartCount;
import movlit.be.book.domain.repository.BookHeartCountRepository;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookDetailWriteService {

    private final BookRepository bookRepository;
    private final BookHeartRepository bookHeartRepository;
    private final BookHeartCountRepository bookHeartCountRepository;

    // 찜하기
    @Transactional
    public BookHeart addHeart(Member member, Book book) {
        BookHeart existingHeart = bookHeartRepository.findByBookAndMember(book, member);
        if (existingHeart == null) {
            // bookHeart 추가
            BookHeart bookHeart = BookHeart.builder()
                    .book(book)
                    .member(member)
                    .isHearted(true)
                    .build();
            BookHeart savedHeart = bookHeartRepository.save(bookHeart);
            // bookHeartCount 증가
            if (savedHeart != null) {
                BookHeartCount existingCount = bookHeartCountRepository.findByBook(book);
                if(existingCount == null){
                    BookHeartCount heartCount = BookHeartCount.builder()
                            .book(book)
                            .build();
                    existingCount = bookHeartCountRepository.save(heartCount);
                }
                bookHeartCountRepository.increaseHeartCount(book);
            }
            return savedHeart;
        } else {
            return existingHeart;
        }
    }

    // 찜하기 제거
    @Transactional
    public void removeHeart(Member member, Book book) throws Exception {
        BookHeart existingHeart = bookHeartRepository.findByBookAndMember(book, member);
        if (existingHeart != null) {
            bookHeartRepository.delete(existingHeart);
            BookHeartCount existingCount = bookHeartCountRepository.findByBook(book);
            if(existingCount != null && existingCount.getCount() > 0)
                bookHeartCountRepository.decreaseHeartCount(book);
        } else {
            throw new Exception("찜하기를 삭제할 수 없습니다");
        }
    }


}
