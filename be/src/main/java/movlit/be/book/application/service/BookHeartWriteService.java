package movlit.be.book.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookHeartCountVo;
import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.repository.BookHeartCountRepository;
import movlit.be.book.domain.repository.BookHeartRepository;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookHeartWriteService {

    private final BookHeartRepository bookHeartRepository;
    private final BookHeartCountRepository bookHeartCountRepository;

    // 해당 도서 찜하기
    @Transactional
    public BookHeartVo addHeart(Member member, BookVo bookVo) {
        BookHeartVo existingHeart = bookHeartRepository.fetchByBookAndMember(bookVo, member);
        if (existingHeart == null) {
            // bookHeart 추가
            BookHeartVo bookHeartVo = BookHeartVo.builder()
                    .bookVo(bookVo)
                    .member(member)
                    .isHearted(true)
                    .build();
            BookHeartVo savedHeart = bookHeartRepository.save(bookHeartVo);
            // bookHeartCount 증가
            if (savedHeart != null) {
                BookHeartCountVo existingCount = bookHeartCountRepository.fetchByBook(bookVo);
                if (existingCount == null) {
                    BookHeartCountVo heartCount = BookHeartCountVo.builder()
                            .bookVo(bookVo)
                            .build();
                    existingCount = bookHeartCountRepository.save(heartCount);
                }
                bookHeartCountRepository.increaseHeartCount(bookVo);
            }
            return savedHeart;
        } else {
            return existingHeart;
        }
    }

    // 도서 찜하기 제거
    @Transactional
    public void removeHeart(Member member, BookVo bookVo) throws Exception {
        BookHeartVo existingHeart = bookHeartRepository.fetchByBookAndMember(bookVo, member);
        if (existingHeart != null) {
            bookHeartRepository.delete(existingHeart);
            BookHeartCountVo existingCount = bookHeartCountRepository.fetchByBook(bookVo);
            if (existingCount != null && existingCount.getCount() > 0) {
                bookHeartCountRepository.decreaseHeartCount(bookVo);
            }
        } else {
            throw new Exception("찜하기를 삭제할 수 없습니다");
        }
    }


}
