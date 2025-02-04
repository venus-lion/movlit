package movlit.be.book.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.service.BookCommentLikeWriteService;
import movlit.be.book.application.service.BookCommentReadService;
import movlit.be.book.application.service.BookCommentWriteService;
import movlit.be.book.application.service.BookDetailReadService;
import movlit.be.book.application.service.BookDetailWriteService;
import movlit.be.book.domain.BookCommentLikeVo;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.presentation.dto.BookCommentRequestDto;
import movlit.be.book.presentation.dto.BookCommentResponseDto;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
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
    private final BookCommentLikeWriteService bookCommentLikeWriteService;
    private final MemberReadService memberReadService;

    // 리뷰 리스트
    @GetMapping("{bookId}/comments")
    public ResponseEntity<Slice<BookCommentResponseDto>> bookCommentReadService(@PathVariable BookId bookId,
                                                                                @AuthenticationPrincipal MyMemberDetails details,
                                                                                @PageableDefault(size = 4, sort = "regDt", direction = Direction.DESC)
                                                                                Pageable pageable) {
        MemberId memberId = null;
        if (details != null) {
            memberId = details.getMemberId();
        }

        Slice<BookCommentResponseDto> pagedResult = bookCommentReadService.fetchPagedBookComments(bookId, memberId,
                pageable);

        log.info("::BookCommentController_bookCommentReadService::");

        return ResponseEntity.ok(pagedResult);
    }

    // 해당 책 나의 리뷰
    @GetMapping("{bookId}/myComment")
    public ResponseEntity fetchMyBookComment(@PathVariable BookId bookId,
                                             @AuthenticationPrincipal MyMemberDetails details) {
        if (details != null) {
            MemberId memberId = details.getMemberId();

            BookCommentResponseDto myBookComment = bookCommentReadService.fetchCommentByMemberAndBook(memberId, bookId);
            if (myBookComment == null) {
                return ResponseEntity.badRequest().build();
            }

            log.info("::BookCommentController_fetchMyBookComment::");
            log.info(">> MyBookComment : " + myBookComment);

            return ResponseEntity.ok(myBookComment);
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
            BookCommentVo savedComment = bookCommentWriteService.registerBookComment(memberId, bookId, commentDto);

            log.info("::BookCommentController_registerComment::");
            log.info(">> SavedComment : " + savedComment);

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

            BookCommentVo updatedComment = bookCommentWriteService.updateBookComment(memberId, bookId, bookCommentId,
                    commentDto);

            log.info("::BookCommentController_updateComment::");
            log.info(">> SavedComment : " + updatedComment);

            return ResponseEntity.ok(updatedComment);
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

            log.info("::BookCommentController_deleteComment::");

            bookCommentWriteService.deleteBookComment(memberId, bookId, bookCommentId);

            return ResponseEntity.ok().build();

        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    // 해당 도서 리뷰 좋아요(like) 하기
    @PostMapping("comments/{bookCommentId}/likes")
    public ResponseEntity addLikes(@PathVariable BookCommentId bookCommentId,
                                   @AuthenticationPrincipal MyMemberDetails details) {
        if (details != null) {
            MemberId memberId = details.getMemberId();

            BookCommentLikeVo savedLike = bookCommentLikeWriteService.addLike(memberId, bookCommentId);

            log.info("::BookCommentController_addLikess::");
            log.info(">> savedLike : " + savedLike);

            return ResponseEntity.ok(savedLike);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 해당 도서 리뷰 좋아요(like) 삭제
    @DeleteMapping("comments/{bookCommentId}/likes")
    public ResponseEntity removeLikes(@PathVariable BookCommentId bookCommentId,
                                      @AuthenticationPrincipal MyMemberDetails details)
            throws Exception {
        if (details != null) {
            MemberId memberId = details.getMemberId();

            bookCommentLikeWriteService.removeLike(memberId, bookCommentId);

            return ResponseEntity.ok().build();
        }
        return
                ResponseEntity.badRequest().build();
    }


}