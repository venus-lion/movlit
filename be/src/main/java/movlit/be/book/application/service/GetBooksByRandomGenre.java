package movlit.be.book.application.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Book;
import movlit.be.book.domain.Genre;
import movlit.be.book.domain.repository.BookGenreRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetBooksByRandomGenre {
    private final BookGenreRepository bookGenreRepository;

    // 랜덤 장르 3개를 뽑고, 해당 Book을 가져오는 함수
    public List<Book> getBooksByRandomGenres(int limit){
        Pageable pageable = PageRequest.of(0, limit);

        List<Genre> genreList = Arrays.asList(Genre.values()); // Genre Enum 목록 가져오기

        Collections.shuffle(genreList); // 장르 무작위 섞기

        List<Long> genreIds = genreList.stream()
                .limit(3) // 3개 제한
                .map(Genre::getId) // ID 추출
                .toList();

        return bookGenreRepository.findBooksByGenreIds(genreIds, pageable);
    }
}
