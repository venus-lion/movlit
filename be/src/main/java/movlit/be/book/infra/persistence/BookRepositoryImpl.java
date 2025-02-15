package movlit.be.book.infra.persistence;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.application.converter.BookConverter;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.infra.persistence.jpa.BookJpaRepository;
import movlit.be.book.presentation.dto.BookCrewResponseDto;
import movlit.be.book.presentation.dto.BookDetailResponseDto;
import movlit.be.common.exception.BookNotFoundException;
import movlit.be.common.exception.BooksByGenreNotFoundException;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final BookJpaRepository bookJpaRepository;

    @Override
    public BookVo fetchByBookId(BookId bookId) {
        BookEntity bookEntity = bookJpaRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        return BookConverter.toDomain(bookEntity);
    }
    @Override
    public List<BookVo> findBooksWithCrewDetails(List<BookId> bookIds) {
        List<BookEntity> booksWithCrewDetails = bookJpaRepository.findBooksWithCrewDetails(bookIds);

        // BOOKS_BY_GENRE_NOT_FOUND
        if (booksWithCrewDetails.isEmpty()){
            throw new BooksByGenreNotFoundException();
        }

        return booksWithCrewDetails.stream()
                .map(bookEntity -> BookConverter.toDomain(bookEntity))
                .collect(Collectors.toList());
    }

    @Override
    public List<BookVo> findBooksByGenreIds(List<Long> genreIds, Pageable pageable) {
        List<BookEntity> booksByGenreIds = bookJpaRepository.findBooksByGenreIds(genreIds, pageable);
        if (booksByGenreIds.isEmpty()){
            throw new BooksByGenreNotFoundException();
        }
        return booksByGenreIds.stream()
                .map(bookEntity -> BookConverter.toDomain(bookEntity))
                .collect(Collectors.toList());
    }

    @Override
    public BookDetailResponseDto fetchBookDetailByBookId(BookId bookId, MemberId memberId) {
        BookDetailResponseDto bookDetailResponse = bookJpaRepository.findBookDetailByBookId(bookId, memberId)
                .orElseThrow(BookNotFoundException::new);

        return bookDetailResponse;
    }

    @Override
    public List<BookCrewResponseDto> fetchBookCrewByBookId(BookId bookId){
        List<BookCrewResponseDto> bookCrewResponse = bookJpaRepository.findBookCrewByBookId(bookId)
                .orElseThrow(BookNotFoundException::new);

        return bookCrewResponse;
    }


}
