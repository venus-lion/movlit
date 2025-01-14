package movlit.be.book.application.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookCommentVo;
import movlit.be.book.domain.BookCommentLikeVo;
import movlit.be.book.domain.BookCommentLikeCountVo;
import movlit.be.book.domain.BookVo;
import movlit.be.book.presentation.dto.BookCommentRequestDto;
import movlit.be.book.domain.entity.GenerateUUID;
import movlit.be.book.domain.repository.BookCommentLikeCountRepository;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookCommentWriteService {

    private final BookCommentReadService bookCommentReadService;

    private final BookCommentRepository bookCommentRepository;
    private final BookCommentLikeRepository bookCommentLikeRepository;
    private final BookCommentLikeCountRepository bookCommentLikeCountRepository;

    // 도서 리뷰 등록
    public BookCommentVo registerBookComment(Member member, BookVo bookVo, BookCommentRequestDto commentDto)
            throws BookCommentAccessDenied {
        // 한 사용자는 하나의 도서에 관해 1개의 리뷰만 등록 가능
        BookCommentVo savedComment = bookCommentRepository.fetchByMemberAndBook(member, bookVo);
        // 첫 리뷰라면 -> 리뷰 저장
        if (savedComment == null) {
            BookCommentVo bookCommentVo = BookCommentVo.builder()
                    .bookCommentId(new BookCommentId(GenerateUUID.generateUUID()))
                    .bookVo(bookVo)
                    .member(member)
                    .comment(commentDto.getComment())
                    .score(commentDto.getScore())
                    .build();
            return bookCommentRepository.save(bookCommentVo);
        } else {
            // 등록된 리뷰가 이미 있다면 -> update
            return updateBookComment(member, bookVo, savedComment.getBookCommentId(), commentDto);
        }


    }

    // 도서 리뷰 수정
    public BookCommentVo updateBookComment(Member member, BookVo bookVo, BookCommentId bookCommentId,
                                           BookCommentRequestDto commentDto)
            throws BookCommentAccessDenied {
        BookCommentVo bookCommentVo = bookCommentReadService.fetchByBookCommentId(bookCommentId);

        if (bookCommentVo != null) {
            if (bookCommentVo.getMember().getMemberId() == member.getMemberId()
                    && bookCommentVo.getBookVo().getBookId() == bookVo.getBookId()) {
                {
                    bookCommentVo.setComment(commentDto.getComment());
                    if (commentDto.getScore() != null) {
                        bookCommentVo.setScore(commentDto.getScore());

                    } else {
                        throw new BookCommentAccessDenied();
                    }

                }
            } else {
                throw new BookCommentAccessDenied();
            }

        }

        return bookCommentRepository.save(bookCommentVo);
    }

    // 도서 리뷰 삭제
    public void deleteBookComment(Member member, BookVo bookVo, BookCommentId bookCommentId)
            throws BookCommentAccessDenied {
        BookCommentVo bookCommentVo = bookCommentReadService.fetchByBookCommentId(bookCommentId);

        if (bookCommentVo != null) {
            if (bookCommentVo.getMember().getMemberId() == member.getMemberId()
                    && bookCommentVo.getBookVo().getBookId() == bookVo.getBookId()) {
                try {
                    // 하드 삭제
                    System.out.println("^^삭제할 코멘트id " + bookCommentId);
                    bookCommentRepository.deleteById(bookCommentId);

                } catch (Exception e) {
                    e.getMessage();
                }
            }
        } else {
            throw new BookCommentAccessDenied();
        }

    }


    // 해당 도서 리뷰에 대한 좋아요 추가
    @Transactional
    public BookCommentLikeVo addLike(Member member, BookVo bookVo, BookCommentVo bookCommentVo) {
        BookCommentLikeVo existingLike = bookCommentLikeRepository.fetchByBookCommentAndMember(bookCommentVo, member);
        System.out.println("&& existingLike : " + existingLike);
        // 해당 리뷰 좋아요 기존 상태 정보 없다면 -> 새로 생성
        if (existingLike == null) {
            // Like 추가
            BookCommentLikeVo bookCommentLikeVo = BookCommentLikeVo.builder()
                    .bookCommentVo(bookCommentVo)
                    .bookVo(bookVo)
                    .member(member)
                    .isLiked(true)
                    .build();
            BookCommentLikeVo savedLike = bookCommentLikeRepository.save(bookCommentLikeVo);
            // bookCommentLikeCount 증가
            if (savedLike != null) {
                BookCommentLikeCountVo existingCount = bookCommentLikeCountRepository.fetchByBookComment(bookCommentVo);
                if (existingCount == null) {
                    BookCommentLikeCountVo likeCount = BookCommentLikeCountVo.builder()
                            .bookCommentVo(bookCommentVo)
                            .count(0)
                            .build();
                    existingCount = bookCommentLikeCountRepository.save(likeCount);
                }
                bookCommentLikeCountRepository.increaseLikeCount(bookCommentVo);
            }
            return savedLike;
        } else {
            return existingLike;
        }

    }

    // 해당 도서 리뷰에 대한 좋아요 삭제
    @Transactional
    public void removeLike(Member member, BookVo bookVo, BookCommentVo bookCommentVo) throws Exception {
        BookCommentLikeVo existingLike = bookCommentLikeRepository.fetchByBookCommentAndMember(bookCommentVo, member);
        if(existingLike != null){
            bookCommentLikeRepository.delete(existingLike);
            BookCommentLikeCountVo existingCount = bookCommentLikeCountRepository.fetchByBookComment(bookCommentVo);
            if(existingCount != null && existingCount.getCount() > 0)
                bookCommentLikeCountRepository.decreaseHeartCount(bookCommentVo);

        }else {
            throw new Exception("좋아요를 삭제할 수 없습니다.");
        }
    }


}