package movlit.be.book.application.service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.Genre;
import movlit.be.book.domain.repository.BookGenreRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto.GenreDto;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto.WriterDto;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBooksByRandomGenreService {
    private final BookGenreRepository bookGenreRepository;
    private final BookRepository bookRepository;

    // 랜덤 장르 3개를 뽑고, 해당 Book을 가져오는 함수
    public List<BookItemWithGenreDto> getBooksByRandomGenres(int limit){
        Pageable pageable = PageRequest.of(0, limit);

        // 랜덤장르 3개 뽑기 (16L 서부와 99999L 기타 장르 제외) - Book Genre에서 서부는 존재하지 않음
        List<Genre> genreList = Arrays.stream(Genre.values())
                .filter(genre -> (genre.getId() != 16L && genre.getId() != 99999L))
                .collect(Collectors.toList());; // Genre Enum 목록 가져오기
        Collections.shuffle(genreList); // 장르 무작위 섞기
        List<Long> genreIds = genreList.stream()
                .limit(3) // 3개 제한
                .map(Genre::getId) // ID 추출
                .toList();

        for (Long genreId : genreIds){ // 3개 뽑힌 장르 확인
            System.out.println("뽑힌 장르 >>>>>>>> " + Genre.of(genreId).getName());
        }
        // 해당 랜덤장르 3개에 속하는 BookList (paging, 디폴트 : 30개)
        List<Book> booksByGenreIds = bookGenreRepository.findBooksByGenreIds(genreIds, pageable);

        // 해당 Book의 id 뽑아내기
        List<BookId> bookIds = booksByGenreIds.stream()
                .map(book -> book.getBookId())
                .collect(Collectors.toList());

        // 해당 Book id에 연관된 엔디티들 한번에 불러오기
        List<Book> booksCrewDetails = bookRepository.findBooksWithCrewDetails(bookIds);

        return booksCrewDetails.stream()
                .map(book -> convertToDto(book))
                .collect(Collectors.toList());
    }

    public BookItemWithGenreDto convertToDto(Book book){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // book -> List<WriterDto>
        List<WriterDto> writerDtos = book.getBookRCrews().stream()
                .map(rc -> WriterDto.builder()
                        .name(rc.getBookcrew().getName())
                        .role(rc.getBookcrew().getRole().name())
                        .build())
                .collect(Collectors.toList());


        // book -> List<GenreDto> ->> 여기서 에러가 남. 해당 장르 id는 유효하지 않대
        List<GenreDto> genreDtos = book.getBookGenres().stream()
                .map(bg -> GenreDto.builder()
                        .genreId(bg.getGenreId())
                        .genreName(Genre.of(bg.getGenreId()).getName())
                        .build())
                .collect(Collectors.toList());

        return BookItemWithGenreDto.builder()
                .bookId(book.getBookId().getValue())
                .title(book.getTitle())
                .writers(writerDtos)
                .genres(genreDtos)
                .pubDate(book.getPubDate().format(formatter))
                .bookImgUrl(book.getBookImgUrl())
                .build();

    }
}
