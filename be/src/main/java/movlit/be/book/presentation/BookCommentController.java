package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.service.BookCommentReadService;
import movlit.be.book.application.service.BookCommentWriteService;
import movlit.be.book.application.service.BookDetailReadService;
import movlit.be.book.application.service.BookDetailWriteService;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookCommentLikeVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.presentation.dto.BookCommentRequestDto;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
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
    @GetMapping("{bookId}/comments")
    public ResponseEntity<Slice<BookCommentResponseDto>> bookCommentReadService(@PathVariable BookId bookId,
                                                                                @AuthenticationPrincipal MyMemberDetails details,
                                                                                @PageableDefault(size = 4, sort = "regDt", direction = Direction.DESC)
                                                                                Pageable pageable) {
        MemberId memberId = null;
        if(details != null)
            memberId = details.getMemberId();

        Slice<BookCommentResponseDto> pagedResult = bookCommentReadService.fetchPagedBookComments(bookId, memberId, pageable);

        return ResponseEntity.ok(pagedResult);
    }

    // 해당 책 나의 리뷰
    @GetMapping("{bookId}/myComment")
    public ResponseEntity myComment(@PathVariable BookId bookId, @AuthenticationPrincipal MyMemberDetails details) {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);
            BookVo bookVo = bookDetailReadService.fetchByBookId(bookId);

            BookCommentVo myComment = bookCommentReadService.fetchByMemberAndBook(member, bookVo);
            if(myComment == null)
                return ResponseEntity.badRequest().build();
            BookCommentResponseDto myCommentRes = BookCommentResponseDto.builder()
                    .bookCommentId(myComment.getBookCommentId())
                    .score(myComment.getScore())
                    .comment(myComment.getComment())
                    .nickname(member.getNickname())
                    .profileImgUrl(member.getProfileImgUrl())
                    .regDt(myComment.getRegDt())
                    .updDt(myComment.getUpdDt())
                    .build();

            return ResponseEntity.ok(myCommentRes);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 도서 리뷰 작성
    @PostMapping("{bookId}/comments")
    public ResponseEntity registerComment(@PathVariable BookId bookId, @RequestBody BookCommentRequestDto commentDto,
                                          @AuthenticationPrincipal MyMemberDetails details)
            throws BookCommentAccessDenied {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);

            BookVo bookVo = bookDetailReadService.fetchByBookId(bookId);
            System.out.println("### 리뷰등록controller member " + member + " book " + bookVo);
            System.out.println("### 리뷰등록controller dto " + commentDto);

            BookCommentVo savedComment = bookCommentWriteService.registerBookComment(member, bookVo, commentDto);
            return ResponseEntity.ok(savedComment);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 도서 리뷰 수정
    @PostMapping("{bookId}/comments/{bookCommentId}")
    public ResponseEntity updateComment(@PathVariable BookId bookId,
                                        @PathVariable BookCommentId bookCommentId,
                                        @RequestBody BookCommentRequestDto commentDto,
                                        @AuthenticationPrincipal MyMemberDetails details)
            throws BookCommentAccessDenied {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);
            System.out.println("$$$$$$$$$$$$ member " + member);

            BookVo bookVo = bookDetailReadService.fetchByBookId(bookId);

            BookCommentVo updatedComment = bookCommentWriteService.updateBookComment(member, bookVo, bookCommentId,
                    commentDto);
            BookCommentResponseDto updatedCommentRes = BookCommentResponseDto.builder()
                    .bookCommentId(updatedComment.getBookCommentId())
                    .score(updatedComment.getScore())
                    .comment(updatedComment.getComment())
                    .nickname(member.getNickname())
                    .profileImgUrl(member.getProfileImgUrl())
                    .regDt(updatedComment.getRegDt())
                    .updDt(updatedComment.getUpdDt())
                    .build();
            return ResponseEntity.ok(updatedCommentRes);
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    // 도서 리뷰 삭제
    @DeleteMapping("{bookId}/comments/{bookCommentId}/delete")
    public ResponseEntity deleteComment(@PathVariable BookId bookId,
                                        @PathVariable BookCommentId bookCommentId,
                                        @AuthenticationPrincipal MyMemberDetails details)
            throws BookCommentAccessDenied {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);
            System.out.println("$$$$$$$$$$$$ member " + member);

            BookVo bookVo = bookDetailReadService.fetchByBookId(bookId);

            bookCommentWriteService.deleteBookComment(member, bookVo, bookCommentId);

            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    // 해당 도서 리뷰 좋아요(like) 하기
    @PostMapping("comments/{bookCommentId}/likes")
    public ResponseEntity addHearts(@PathVariable BookCommentId bookCommentId,
                                    @AuthenticationPrincipal MyMemberDetails details) {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);

            BookCommentVo comment = bookCommentReadService.fetchByBookCommentId(bookCommentId);
            BookVo bookVo = bookDetailReadService.fetchByBookId(comment.getBookVo().getBookId());
            System.out.println("&& bookComment : " + comment + " book : " + bookVo);

            BookCommentLikeVo savedLike = bookCommentWriteService.addLike(member, bookVo, comment);

            return ResponseEntity.ok(savedLike);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 해당 도서 리뷰 좋아요(like) 삭제
    @DeleteMapping("comments/{bookCommentId}/likes")
    public ResponseEntity removeHearts(@PathVariable BookCommentId bookCommentId,
                                       @AuthenticationPrincipal MyMemberDetails details)
            throws Exception {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            Member member = memberReadService.findByMemberId(memberId);


            BookCommentVo comment = bookCommentReadService.fetchByBookCommentId(bookCommentId);
            BookVo bookVo = bookDetailReadService.fetchByBookId(comment.getBookVo().getBookId());

            bookCommentWriteService.removeLike(member, bookVo, comment);

            return ResponseEntity.ok().build();
        }
        return
                ResponseEntity.badRequest().build();
    }

}