package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.converter.service.BookDetailReadService;
import movlit.be.book.application.converter.service.BookDetailWriteService;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookHeart;
import movlit.be.book.domain.dto.BookDetailResponseDto;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
    public BookDetailResponseDto getBookDetail(@PathVariable BookId bookId,
                                               @AuthenticationPrincipal MyMemberDetails details) {
        Member member = null;
        if(details != null){
            MemberId memberId = details.getMemberId();
            member = memberReadService.findByMemberId(memberId);
        }

        BookDetailResponseDto bookDetailResponse = bookDetailReadService.getBookDetail(bookId, member);
        if (details != null) {
            System.out.println("&&detail 있니? : " + details);
            System.out.println("&&detail의 mem정보" + details.getMemberId());
            System.out.println("&&detail의 mem이름" + details.getUsername());
        }

        return bookDetailResponse;
    }

    // 도서 찜(heart)하기
    @PostMapping("{bookId}/hearts")
    public ResponseEntity addHearts(@PathVariable BookId bookId, @AuthenticationPrincipal MyMemberDetails details) {
        if (details != null) {
            MemberId memberId = details.getMemberId();

            Member member = memberReadService.findByMemberId(memberId);
            Book book = bookDetailReadService.findByBookId(bookId);

            BookHeart savedHeart = bookDetailWriteService.addHeart(member, book);
            return ResponseEntity.ok(savedHeart);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 도서 찜(heart) 삭제
    @DeleteMapping("{bookId}/hearts")
    public ResponseEntity removeHearts(@PathVariable BookId bookId, @AuthenticationPrincipal MyMemberDetails details
    ) throws Exception {
        if (details != null) {
            MemberId memberId = details.getMemberId();

            Member member = memberReadService.findByMemberId(memberId);

            Book book = bookDetailReadService.findByBookId(bookId);


            bookDetailWriteService.removeHeart(member, book);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

}