package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.converter.service.BookCommentReadService;
import movlit.be.book.application.converter.service.BookCommentWriteService;
import movlit.be.book.application.converter.service.BookDetailReadService;
import movlit.be.book.application.converter.service.BookDetailWriteService;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.dto.BookCommentRequestDto;
import movlit.be.book.domain.dto.BookCommentsResponseDto;
import movlit.be.book.domain.entity.BookCommentEntity;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.movie.presentation.dto.response.MovieCommentReadResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
//    public ResponseEntity<Slice<BookCommentsResponseDto>>  bookCommentReadService(@PathVariable BookId bookId,
//                                                                                   @AuthenticationPrincipal MyMemberDetails details,
//                                                                                   @PageableDefault(size = 4, sort = "regDt", direction = Direction.DESC)
//                                                     Pageable pageable){
//        //Book book = bookDetailReadService.findByBookId(bookId);
//        Slice<BookCommentsResponseDto> pagedResult = bookCommentReadService.getPagedBookComments(bookId, pageable);
//
//
//       return ResponseEntity.ok(pagedResult);
//    }

    @GetMapping("/books/{bookId}/myComment")
    public ResponseEntity myComment(@PathVariable BookId bookId, @AuthenticationPrincipal MyMemberDetails details) {
        if(details != null){
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);
            Book book = bookDetailReadService.findByBookId(bookId);

            BookComment myComment = bookCommentReadService.findByMemberAndBook(member, book);


            return ResponseEntity.ok(myComment);
        }else
            return ResponseEntity.badRequest().build();
    }

    // 도서 리뷰 작성
    @PostMapping("{bookId}/comments")
    public ResponseEntity registerComment(@PathVariable BookId bookId, @RequestBody BookCommentRequestDto commentDto,
                                  @AuthenticationPrincipal MyMemberDetails details) throws BookCommentAccessDenied {
        if(details != null){
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);
            System.out.println("$$$$$$$$$$$$ member " + member);


            Book book = bookDetailReadService.findByBookId(bookId);
            //Member member = memberReadService.findByMemberId(memberId);
            System.out.println("### 리뷰등록controller member " + member +  " book " + book);
            System.out.println("### 리뷰등록controller dto " + commentDto );

            BookComment savedComment = bookCommentWriteService.registerBookComment(member, book, commentDto);
            return ResponseEntity.ok(savedComment);
        }else
            return ResponseEntity.badRequest().build();
    }

    // 도서 리뷰 수정
    @PostMapping("{bookId}/comments/{bookCommentId}")
    public ResponseEntity updateComment(@PathVariable BookId bookId,
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
    public ResponseEntity deleteComment(@PathVariable BookId bookId,
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
    public ResponseEntity addHearts(@PathVariable BookId bookId,
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
    public ResponseEntity removeHearts(@PathVariable BookId bookId,
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
