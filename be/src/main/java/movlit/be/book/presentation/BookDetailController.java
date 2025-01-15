package movlit.be.book.presentation;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.auth.application.service.MyMemberDetails;
import movlit.be.book.application.service.BookDetailReadService;
import movlit.be.book.application.service.BookDetailWriteService;
import movlit.be.book.domain.BookHeartVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.presentation.dto.BookDetailResponseDto;
import movlit.be.bookES.BookESDomain;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.member.domain.Member;
import movlit.be.movie.domain.document.MovieDocument;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public BookDetailResponseDto fetchBookDetail(@PathVariable BookId bookId,
                                                 @AuthenticationPrincipal MyMemberDetails details) {
        Member member = null;
        MemberId memberId = null;
        if (details != null) {
            memberId = details.getMemberId();
            member = memberReadService.findByMemberId(memberId);
        }

        BookDetailResponseDto detailResponse = bookDetailReadService.fetchBookDetail(bookId, memberId);

        System.out.println("//책 상세 : \n" + detailResponse.toString());
        return detailResponse;
    }

    // 도서 찜(heart)하기
    @PostMapping("{bookId}/hearts")
    public ResponseEntity addHearts(@PathVariable BookId bookId, @AuthenticationPrincipal MyMemberDetails details) {
        if (details != null) {
            MemberId memberId = details.getMemberId();
            System.out.println("##책 ID --> " + bookId.getValue());

            Member member = memberReadService.findByMemberId(memberId);
            BookVo bookVo = bookDetailReadService.fetchByBookId(bookId);

            BookHeartVo savedHeart = bookDetailWriteService.addHeart(member, bookVo);
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

            BookVo bookVo = bookDetailReadService.fetchByBookId(bookId);

            bookDetailWriteService.removeHeart(member, bookVo);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }


    }

    @GetMapping("{bookId}/recommendedBooks")
    public ResponseEntity<List<BookESDomain>> fetchRecommendedBooks(@PathVariable BookId bookId) {
        List<BookESDomain> recommendedBookList = bookDetailReadService.fetchRecommendedBooks(bookId);
        for (BookESDomain recBook : recommendedBookList) {
            // 각 BookCommentResponseDto의 내용 출력
            System.out.println("@@ 추천 책 제목 : " + recBook.getTitle());
            System.out.println("@@ 추천 책 카테고리: " + recBook.getCategoryName());
            System.out.println("@@ 추천 책 설명 : " + recBook.getDescription());
        }
        return ResponseEntity.ok(recommendedBookList);
    }

    @GetMapping("{bookId}/recommendedMovies")
    public ResponseEntity<List<MovieDocument>> fetchRecommendedMovies(@PathVariable BookId bookId) {
        List<MovieDocument> recommendedMovieList = bookDetailReadService.fetchRecommendedMovies(bookId);
        for (MovieDocument recMovie : recommendedMovieList) {
            // 각 MovieDocument의 내용 출력
            System.out.println("@@ 추천 영화 제목 : " + recMovie.getTitle());
            System.out.println("@@ 추천 영화 카테고리: " + recMovie.getMovieGenre().stream());
            System.out.println("@@ 추천 영화 설명 : " + recMovie.getOverview());
        }
        return ResponseEntity.ok(recommendedMovieList);
    }


}