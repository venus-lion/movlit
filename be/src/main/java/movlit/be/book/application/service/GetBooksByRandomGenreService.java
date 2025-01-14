package movlit.be.book.application.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookVo;
import movlit.be.book.domain.Genre;
import movlit.be.book.domain.repository.BookGenreRepository;
import movlit.be.book.domain.repository.BookRepository;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto.GenreDto;
import movlit.be.book.presentation.dto.BooksGenreResponse.BookItemWithGenreDto.WriterDto;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.domain.repository.MemberGenreRepository;
import movlit.be.movie.domain.repository.MovieGenreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBooksByRandomGenreService {

    private final BookGenreRepository bookGenreRepository;
    private final BookRepository bookRepository;
    private final MemberGenreRepository memberGenreRepository;
    private final MovieGenreRepository movieGenreRepository;

    // 랜덤 장르 3개를 뽑고, 해당 Book을 가져오는 함수
    public List<BookItemWithGenreDto> getBooksByRandomGenres(int limit) {
        Pageable pageable = PageRequest.of(0, limit);

        // 랜덤장르 3개 뽑기 (16L 서부와 99999L 기타 장르 제외) - Book Genre에서 서부는 존재하지 않음
        List<Genre> genreList = Arrays.stream(Genre.values())
                .filter(genre -> (genre.getId() != 16L && genre.getId() != 99999L))
                .collect(Collectors.toList());
        ; // Genre Enum 목록 가져오기
        Collections.shuffle(genreList); // 장르 무작위 섞기
        List<Long> genreIds = genreList.stream()
                .limit(3) // 3개 제한
                .map(Genre::getId) // ID 추출
                .toList();

        for (Long genreId : genreIds) { // 3개 뽑힌 장르 확인
            System.out.println("뽑힌 장르 >>>>>>>> " + Genre.of(genreId).getName());
        }
        // 해당 랜덤장르 3개에 속하는 BookList (paging, 디폴트 : 30개)
        List<BookVo> booksByGenreIds = bookGenreRepository.findBooksByGenreIds(genreIds, pageable);

        // 해당 Book의 id 뽑아내기
        List<BookId> bookIds = booksByGenreIds.stream()
                .map(book -> book.getBookId())
                .collect(Collectors.toList());

        // 해당 Book id에 연관된 엔디티들 한번에 불러오기
        List<BookVo> booksCrewDetails = bookRepository.findBooksWithCrewDetails(bookIds);

        return booksCrewDetails.stream()
                .map(book -> convertToDto(book))
                .collect(Collectors.toList());
    }

    public BookItemWithGenreDto convertToDto(BookVo bookVo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // book -> List<WriterDto>
        List<WriterDto> writerDtos = bookVo.getBookRCrews().stream()
                .map(rc -> WriterDto.builder()
                        .name(rc.getBookcrew().getName())
                        .role(rc.getBookcrew().getRole().name())
                        .build())
                .collect(Collectors.toList());

        // book -> List<GenreDto> ->> 여기서 에러가 남. 해당 장르 id는 유효하지 않대
        List<GenreDto> genreDtos = bookVo.getBookGenres().stream()
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

    public List<BookItemWithGenreDto> getBooksByPersonalizedRandomGenre(int limit, MemberId memberId) {
        Pageable pageable = PageRequest.of(0, limit);
        // 1. 로그인한 Member의 선택 장르(MemberGenre) 가져오기 - 3개 라고 가정
        List<Long> memberGenreIds = memberGenreRepository.findGenreIdsByMemberId(memberId);
        // 2. 멤버가 선택한 장르 2개 랜덤으로 선택
        Collections.shuffle(memberGenreIds);
        List<Long> selectedMemberGenreIds = memberGenreIds.stream()
                .limit(2)
                .collect(Collectors.toList());
        // 3. 전체 장르 중, 기타(99999L), 서부(16L), 멤버가 선택한 장르 제외 - 필터링
        List<Genre> remainingGenres = Arrays.stream(Genre.values())
                .filter(genre -> (genre.getId() != 16L && genre.getId() != 99999L && !memberGenreIds.contains(
                        genre.getId())))
                .collect(Collectors.toList());
        // 4. 남은 장르 중, 랜덤으로 1개 선택
        Collections.shuffle(remainingGenres);
        Long randomGenreId = remainingGenres.get(0).getId();
        // 5. 최종적으로 3개의 장르 ID를 조합
        List<Long> genreIdsForQuery = new ArrayList<>(selectedMemberGenreIds);
        genreIdsForQuery.add(randomGenreId);
        for (Long genreId : genreIdsForQuery) {
            System.out.println("최종적으로 뽑힌 멤버장르 3개 ::: " + genreId);
        }
        // 6. 최종 장르 ID 3개 -> Dto로 변환
        List<BookVo> bookVos = bookRepository.findBooksByGenreIds(genreIdsForQuery, pageable);
        return bookVos.stream()
                .map(book -> convertToDto(book))
                .collect(Collectors.toList());
    }

    public List<BookItemWithGenreDto> fetchBooksByMovieDetailGenres(int limit, Long movieId) {
        Pageable pageable = PageRequest.of(0, limit);
        List<movlit.be.common.util.Genre> movieGenreList = movieGenreRepository.fetchMovieDetailGenresByMovieId(
                movieId);
        List<Long> genreIds = movieGenreList.stream()
                .limit(3) // 3개 제한
                .map(movlit.be.common.util.Genre::getId) // ID 추출
                .toList();

        // 해당 랜덤장르 3개에 속하는 BookList (paging, 디폴트 : 30개)
        List<BookVo> booksByGenreIds = bookGenreRepository.findBooksByGenreIds(genreIds, pageable);

        // 해당 Book의 id 뽑아내기
        List<BookId> bookIds = booksByGenreIds.stream()
                .map(BookVo::getBookId)
                .collect(Collectors.toList());

        // 해당 Book id에 연관된 엔디티들 한번에 불러오기
        List<BookVo> booksCrewDetails = bookRepository.findBooksWithCrewDetails(bookIds);

        return booksCrewDetails.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

}
