package movlit.be.book.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookHeartCountVo;
import movlit.be.book.domain.BookVo;
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



}
