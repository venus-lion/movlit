package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.book.application.converter.service.BookDetailReadService;
import movlit.be.book.application.converter.service.BookDetailWriteService;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookDetailResponseDto;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookDetailController {

    private final BookDetailReadService bookDetailReadService;
    private final BookDetailWriteService bookDetailWriteService;
    private final MemberReadService memberReadService;

    // 해당 도서 상세 내역
    @GetMapping("{bookId}/detail")
    public BookDetailResponseDto getBookDetail(@PathVariable BookId bookId) {
        BookDetailResponseDto bookDetailResponse = bookDetailReadService.getBookDetail(bookId);

        return bookDetailResponse;
    }

    // 도서 찜(heart)하기
    @PostMapping("{bookId}/hearts/{memberTemp}")
    public ResponseEntity addHearts(@PathVariable BookId bookId,
                                    @PathVariable String memberTemp) {
        //임시 - @CurrentMemberId 혹은 세션의 memberId로 원복필요
        System.out.println("####멤버ID#### --> " + memberTemp);
        Member member = memberReadService.findByMemberId(new MemberId(memberTemp));

        //Member member = memberReadService.findByMemberId(memberId);
        Book book = bookDetailReadService.findByBookId(bookId);

        bookDetailWriteService.addHeart(member, book);
        return ResponseEntity.ok().build();
    }

    // 도서 찜(heart) 삭제
    @DeleteMapping("{bookId}/hearts/{memberTemp}")
    public ResponseEntity removeHearts(@PathVariable BookId bookId,
                                       @PathVariable String memberTemp) throws Exception {
        //임시 - @CurrentMemberId 혹은 세션의 memberId로 원복필요
        System.out.println("####멤버ID#### --> " + memberTemp);
        Member member = memberReadService.findByMemberId(new MemberId(memberTemp));

        //Member member = memberReadService.findByMemberId(memberId);
        Book book = bookDetailReadService.findByBookId(bookId);

        bookDetailWriteService.removeHeart(member, book);
        return ResponseEntity.ok().build();
    }


}