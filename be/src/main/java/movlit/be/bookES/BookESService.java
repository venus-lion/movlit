package movlit.be.bookES;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.infra.persistence.jpa.BookJpaRepository;
import org.springframework.stereotype.Service;

// Book 엔디티 -> BookES save 파싱하기
//# DB -> bookcrew의 role_type : ROLE_TYPE :: author만 필터링하기 (작가)값만 가져오게끔
@Service
@RequiredArgsConstructor
public class BookESService {

    private final BookESRepository bookESRepository;
    private final BookJpaRepository bookJpaRepository;

    // 전달받은 bookIds를 기준으로, Elasticsearch에서 데이터를 조회하는 메서드
    public List<BookES> fetchAllBookESByBookIds(List<String> bookIds) {
        Iterable<BookES> bookESList = bookESRepository.findAllById(bookIds);

        return StreamSupport.stream(bookESList.spliterator(), false)
                .toList(); // 결과가 없으면 빈 리스트 반환
    }

    public void saveBookESIndex() {
        List<BookEntity> bookEntitiesAndRelated = bookJpaRepository.findBookEntitiesAndRelated();

        List<BookEntity> filteredBookEntities = bookEntitiesAndRelated.stream()
                .filter(bookEntity -> bookEntity.getBookId() != null && bookEntity.getBookId().getValue() != "")
                .collect(Collectors.toList());

        List<BookES> bookESList = filteredBookEntities.stream()
                .map(bookEntity -> convertToBookES(bookEntity))
                .collect(Collectors.toList());

        // bookES save - repository

        for (BookES bookES : bookESList) {
            bookESRepository.save(bookES);
        }

    }

    // BookEntity -> BookES 바꿔주기
    public BookES convertToBookES(BookEntity bookEntity) {

        String bookId = bookEntity.getBookId().getValue();

        List<String> crewList = bookEntity.getBookRCrewEntities().stream()
                .map(bookRCrewEntity -> bookRCrewEntity.getBookcrewEntity().getName())
                .collect(Collectors.toList());

        String titleKeyword = extractSeriesTitle(bookEntity.getTitle());

        return BookES.builder()
                .bookId(bookId)
                .isbn(bookEntity.getIsbn())
                .title(bookEntity.getTitle())
                .titleKeyword(titleKeyword)
                .crew(crewList)
                .publisher(bookEntity.getPublisher())
                .pubDate(bookEntity.getPubDate().toLocalDate())
                .description(bookEntity.getDescription())
                .categoryName(bookEntity.getCategoryName())
                .bookImgUrl(bookEntity.getBookImgUrl())
                .regDt(bookEntity.getRegDt().toLocalDate())
                .updDt(bookEntity.getUpdDt().toLocalDate())
                .build();
    }

    // title에서 시리즈 제목을 추출하는 메서드
    private String extractSeriesTitle(String title) {
        // "해리 포터와 마법사의 돌 2 (무선)" -> "해리 포터"
        // "위키드 1 - 엘파바와 글린다" -> "위키드"
        // "나 혼자만 레벨업 12 - 만화" -> "나 혼자만 레벨업"
        // 숫자와 괄호를 기준으로 필터링해보자
//        if (title.matches(".+와 .+ \\d+.*")) { // "해리 포터와 마법사의 돌 2 (무선)"
//            return title.substring(0, title.indexOf("와")).trim(); // "해리 포터"
//        } else if (title.matches(".+ \\d+ - .+")) { // "위키드 1 - 엘파바와 글린다"
//            return title.substring(0, title.indexOf(" ")).trim(); // "위키드"
//        } else if (title.matches(".+ \\d+.*")) { // "나 혼자만 레벨업 12 - 만화"
//            return title.substring(0, title.indexOf(" ")).trim(); // "나 혼자만 레벨업"
//        } else {
//            return title; // 시리즈 제목 추출 불가 시 title 그대로 반환
//        }

        // 숫자 전까지 자르기
        int index = 0;
        while (index < title.length() && !Character.isDigit(title.charAt(index))) {
            index++;
        }
        String result = title.substring(0, index).trim();

        // 괄호 전까지 자르기
        index = result.indexOf('(');
        if (index != -1) {
            result = result.substring(0, index).trim();
        }

        // 마지막에 '.' 또는 '-'가 오는 경우 제거
        if (result.endsWith(".") || result.endsWith("-")) {
            result = result.substring(0, result.length() - 1).trim();
        }

        return result;

    }

}
