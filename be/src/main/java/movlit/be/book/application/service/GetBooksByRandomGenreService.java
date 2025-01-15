package movlit.be.book.application.service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookVo;
import movlit.be.common.util.Genre;
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
    public List<BookItemWithGenreDto> fetchBooksByRandomGenre(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        // 랜덤장르 3개 가져오기
        List<Long> randomGenreIds = getRandomGenreIds(3);

        // 해당 랜덤장르 3개에 속하는 BookList (paging, 디폴트 : 30개)
        List<BookId> bookIds = bookGenreRepository.findBooksByGenreIds(randomGenreIds, pageable);

        // 해당 Book id에 연관된 엔디티들 한번에 불러오기
        List<BookVo> booksCrewDetails = bookRepository.findBooksWithCrewDetails(bookIds);

        return booksCrewDetails.stream()
                .map(book -> convertToDto(book))
                .collect(Collectors.toList());
    }

    // 랜덤 장르ID 리스트 가져오기
    private List<Long> getRandomGenreIds(int count){
        List<Genre> genreList = Arrays.stream(Genre.values())
                .filter(genre -> (genre.getId() != 16L && genre.getId() != 99999L))
                .collect(Collectors.toList());

        Collections.shuffle(genreList); // 장르 무작위 섞기

        return genreList.stream()
                .limit(count) // 지정된 개수만큼 제한
                .map(Genre::getId) // ID 추출
                .collect(Collectors.toList());
    }

    // Bookvo를 BookItemWithGenreDto로 변환
    public BookItemWithGenreDto convertToDto(BookVo bookVo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // book -> List<WriterDto>
        List<WriterDto> writerDtos = bookVo.getBookRCrewVos().stream()
                .map(rc -> WriterDto.builder()
                        .name(rc.getBookcrewVo().getName())
                        .role(rc.getBookcrewVo().getRole().name())
                        .build())
                .collect(Collectors.toList());

        // book -> List<GenreDto>
        List<GenreDto> genreDtos = bookVo.getBookGenreVos().stream()
                .map(bg -> GenreDto.builder()
                        .genreId(bg.getGenreId())
                        .genreName(Genre.of(bg.getGenreId()).getName())
                        .build())
                .collect(Collectors.toList());

        return BookItemWithGenreDto.builder()
                .bookId(bookVo.getBookId().getValue())
                .title(bookVo.getTitle())
                .writers(writerDtos)
                .genres(genreDtos)
                .pubDate(bookVo.getPubDate().format(formatter))
                .bookImgUrl(bookVo.getBookImgUrl())
                .build();

    }



}
