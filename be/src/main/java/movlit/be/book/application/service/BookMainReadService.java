package movlit.be.book.application.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.BookBestseller;
import movlit.be.book.domain.repository.BookBestsellerRepository;
import movlit.be.book.presentation.dto.BookBestsellersResponse.BookBestsellerDto;
import movlit.be.book.presentation.dto.BookBestsellersResponse.BookBestsellerDto.WriterDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookMainReadService {
    private final BookBestsellerRepository bookBestsellerRepository;

    public List<BookBestsellerDto> getTopBestsellers(int limit){
        Pageable pageable = PageRequest.of(0, limit); // 0페이지부터 limit 개만큼 가져옴
        List<BookBestseller> bookbestsellers = bookBestsellerRepository.findAllBestsellers(pageable);

        return bookbestsellers.stream()
                .map(bookbestseller -> ModelToDto(bookbestseller))
                .collect(Collectors.toList());
    }

    // BookBestseller -> BookBestsellerDto로 변환
    private BookBestsellerDto ModelToDto(BookBestseller bookBestseller){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<WriterDto> writers = bookBestseller.getBook().getBookRCrews().stream()
                .map(rc -> WriterDto.builder()
                        .name(rc.getBookcrew().getName())
                        .role(rc.getBookcrew().getRole().name())
                        .build())
                .collect(Collectors.toList());

        return BookBestsellerDto.builder()
                .bookId(bookBestseller.getBookBestsellerId())
                .title(bookBestseller.getBook().getTitle()) // BookBestseller -> Book 호출 (N+1)
                .writers(writers) // BookBestseller -> Book -> BookRCrew -> BookCrew 호출 (N+1)
                .pubDate(bookBestseller.getBook().getPubDate().format(formatter))
                .bookImgUrl(bookBestseller.getBook().getBookImgUrl())
                .build();
    }
}
