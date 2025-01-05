package movlit.be.book.application.converter.service;

import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.BookComment;
import movlit.be.book.domain.BookCommentLike;
import movlit.be.book.domain.dto.BookCommentRequestDto;
import movlit.be.book.domain.entity.GenerateUUID;
import movlit.be.book.domain.repository.BookCommentLikeRepository;
import movlit.be.book.domain.repository.BookCommentRepository;
import movlit.be.common.exception.BookCommentAccessDenied;
import movlit.be.common.util.ids.BookCommentId;
import movlit.be.member.domain.Member;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCommentWriteService {
    private final BookCommentReadService bookCommentReadService;

    private final BookCommentRepository bookCommentRepository;
    private final BookCommentLikeRepository bookCommentLikeRepository;




    // 도서 리뷰 등록
    public BookComment registerBookComment(Member member, Book book, BookCommentRequestDto commentDto)
            throws BookCommentAccessDenied {
        // 한 사용자는 하나의 도서에 관해 1개의 리뷰만 등록 가능
        BookComment savedComment = bookCommentRepository.findByMemberAndBook(member, book);
        // 첫 리뷰라면 -> 리뷰 저장
        if(savedComment == null){
            BookComment bookComment = BookComment.builder()
                    .bookCommentId(new BookCommentId(GenerateUUID.generateUUID()))
                    .book(book)
                    .member(member)
                    .comment(commentDto.getComment())
                    .score(commentDto.getScore())
                    .build();
            return bookCommentRepository.save(bookComment);
        }else{
            // 등록된 리뷰가 이미 있다면 -> update
            return updateBookComment(member, book, savedComment.getBookCommentId(), commentDto);
        }


    }

    // 도서 리뷰 수정
    public BookComment updateBookComment(Member member, Book book, BookCommentId bookCommentId,
                                         BookCommentRequestDto commentDto)
            throws BookCommentAccessDenied {
        BookComment bookComment = bookCommentReadService.findByBookCommentId(bookCommentId);

        if (bookComment != null) {
            if (bookComment.getMember().getMemberId() == member.getMemberId()
                    && bookComment.getBook().getBookId() == book.getBookId()) {
                {
                    bookComment.setComment(commentDto.getComment());
                    if (commentDto.getScore() != null) {
                        bookComment.setScore(commentDto.getScore());

                    } else {
                        throw new BookCommentAccessDenied();
                    }

                }
            } else {
                throw new BookCommentAccessDenied();
            }

        }

        return bookCommentRepository.save(bookComment);
    }

    public void deleteBookComment(Member member, Book book, BookCommentId bookCommentId)
            throws BookCommentAccessDenied {
        BookComment bookComment = bookCommentReadService.findByBookCommentId(bookCommentId);

        if (bookComment != null) {
            if (bookComment.getMember().getMemberId() == member.getMemberId()
                    && bookComment.getBook().getBookId() == book.getBookId()) {
                try{
                    // 하드 삭제
                    System.out.println("^^삭제할 코멘트id " + bookCommentId);
                    bookCommentRepository.deleteById(bookCommentId);

                }catch (Exception e){
                    e.getMessage();
                }
            }
        } else {
            throw new BookCommentAccessDenied();
        }

    }


    // 해당 도서 리뷰에 대한 좋아요 추가
    public BookCommentLike addLike(Member member, Book book, BookComment bookComment) {
        BookCommentLike bookCommentLike = bookCommentReadService.myCommentLikeStatus(member, book, bookComment);
        // 해당 리뷰 좋아요 기존 상태 정보 없다면 -> 새로 생성
        if(bookCommentLike == null){
            bookCommentLike = BookCommentLike.builder()
                    .bookComment(bookComment)
                    .book(book)
                    .member(member)
                    .isLiked(true)
                    .build();
        }else{
            // 해당 리뷰 좋아요 기존 상태 정보 있다면 -> 현재 좋아요 상태 반대로 update
            boolean oldLiked = bookCommentLike.getIsLiked();
            bookCommentLike.setIsLiked(!oldLiked);
        }

        return bookCommentLikeRepository.save(bookCommentLike);

    }

    // 해당 도서 리뷰에 대한 좋아요 삭제
    public BookCommentLike removeLike(Member member, Book book, BookComment bookComment){
        return null;
    }



}