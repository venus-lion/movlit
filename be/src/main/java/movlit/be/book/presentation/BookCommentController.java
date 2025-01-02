package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.book.application.converter.service.BookCommentReadService;
import movlit.be.book.application.converter.service.BookCommentWriteService;
import movlit.be.book.application.converter.service.BookDetailReadService;
import movlit.be.book.application.converter.service.BookDetailWriteService;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentRequestDto;
import movlit.be.common.annotation.CurrentMemberId;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookCommentController {
    private final BookDetailReadService bookDetailReadService;
    private final BookDetailWriteService bookDetailWriteService;
    private final BookCommentReadService bookCommentReadService;
    private final BookCommentWriteService bookCommentWriteService;
    private final MemberReadService memberReadService;

    // 리뷰 리스트
    //    @GetMapping("{bookId}/comments")
//    @ResponseBody
//    public List<BookComment> bookCommentReadService(@PathVariable BookId bookId, @RequestParam(name = "p", defaultValue = "1") int page){
//        Page<BookComment> pagedResult = bookCommentReadService.getPagedBookComments(bookId, page);
//        int totalPages = pagedResult.getTotalPages();
//        int startPage = (int) Math.ceil((page - 0.5) / bookCommentReadService.PAGE_SIZE - 1) * bookCommentReadService.PAGE_SIZE + 1;
//        int endPage = Math.min(startPage + bookCommentReadService.PAGE_SIZE - 1, totalPages);
//        List<Integer> pageList = new ArrayList<>();
//        for (int i = startPage; i <= endPage; i++)
//            pageList.add(i);
//
//       return pagedResult.getContent();
//
//    }

    // 도서 리뷰 작성
    @PostMapping("{bookId}/comments")
    public String registerComment(@CurrentMemberId MemberId memberId, @PathVariable BookId bookId, @RequestBody BookCommentRequestDto commentDto) {
        // 임시 (추후 requestbody대신 session으로 memberId/email 받아오기)
        MemberId memberIdTemp = new MemberId(commentDto.getMemberId());
        System.out.println("############# memberID : " +memberIdTemp);
        Member member = memberReadService.findByMemberId(memberIdTemp);
        System.out.println("$$$$$$$$$$$$ member " + member);


        Book book = bookDetailReadService.findByBookId(bookId);
        //Member member = memberReadService.findByMemberId(memberId);

        bookCommentWriteService.registerBookComment(member, book, commentDto);
        return "성공";
    }

    // 도서 리뷰 수정
    @PostMapping("{bookId}/comments/{bookCommentId}")
    public ResponseEntity updateComment(@CurrentMemberId MemberId memberId, @PathVariable BookId bookId,
                                        @PathVariable BookCommentId bookCommentId, @RequestBody BookCommentRequestDto commentDto)
            throws BookCommentAccessDenied {
        // 임시 (추후 requestbody대신 session으로 memberId/email 받아오기)
        System.out.println("############# memberID : " +commentDto.getMemberId());
        MemberId memberIdTemp = new MemberId(commentDto.getMemberId());
        Member member = memberReadService.findByMemberId(memberIdTemp);
        System.out.println("$$$$$$$$$$$$ member " + member);


        Book book = bookDetailReadService.findByBookId(bookId);
        // Member member = memberReadService.findByMemberId(memberId);

        bookCommentWriteService.updateBookComment(member, book, bookCommentId, commentDto);
        return ResponseEntity.ok().build();
    }


    // 도서 리뷰 삭제 - commentDto reuestBody 추후 삭제
    @PostMapping("{bookId}/comments/{bookCommentId}/delete")
    public ResponseEntity deleteComment(@CurrentMemberId MemberId memberId, @PathVariable BookId bookId,
                                        @PathVariable BookCommentId bookCommentId, @RequestBody BookCommentRequestDto commentDto)
            throws BookCommentAccessDenied {

        System.out.println("############# memberID : " +commentDto.getMemberId());
        MemberId memberIdTemp = new MemberId(commentDto.getMemberId());
        Member member = memberReadService.findByMemberId(memberIdTemp);
        System.out.println("$$$$$$$$$$$$ member " + member);


        Book book = bookDetailReadService.findByBookId(bookId);
        // Member member = memberReadService.findByMemberId(memberId);

        bookCommentWriteService.deleteBookComment(member, book, bookCommentId, commentDto);


        return ResponseEntity.ok().build();
    }

    // 해당 도서 리뷰 좋아요(like) 하기
    @PostMapping("{bookId}/comments/{bookCommentId}/likes/{memberTemp}")
    public ResponseEntity addHearts(@CurrentMemberId MemberId memberId, @PathVariable BookId bookId,
                                    @PathVariable BookCommentId bookCommentId, @PathVariable String memberTemp){
        //임시 - @CurrentMemberId 혹은 세션의 memberId로 원복필요
        System.out.println("####멤버ID#### --> " + memberTemp);
        Member member = memberReadService.findByMemberId(new MemberId(memberTemp));

        //Member member = memberReadService.findByMemberId(memberId);
        Book book = bookDetailReadService.findByBookId(bookId);
        BookComment comment = bookCommentReadService.findByBookCommentId(bookCommentId);

        bookCommentWriteService.addLike(member, book, comment);

        return ResponseEntity.ok().build();
    }

    // 해당 도서 리뷰 좋아요(like) 삭제
    @DeleteMapping("{bookId}/comments/{bookCommentId}/likes/{memberTemp}")
    public ResponseEntity removeHearts(@CurrentMemberId MemberId memberId, @PathVariable BookId bookId,
                                       @PathVariable BookCommentId bookCommentId, @PathVariable String memberTemp)
            throws Exception {
        //임시 - @CurrentMemberId 혹은 세션의 memberId로 원복필요
        System.out.println("####멤버ID#### --> " + memberTemp);
        Member member = memberReadService.findByMemberId(new MemberId(memberTemp));

        //Member member = memberReadService.findByMemberId(memberId);
        Book book = bookDetailReadService.findByBookId(bookId);
        BookComment comment = bookCommentReadService.findByBookCommentId(bookCommentId);

        bookCommentWriteService.removeLike(member, book, comment);

        return ResponseEntity.ok().build();
    }

}
