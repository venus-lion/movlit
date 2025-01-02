package movlit.be.bookES;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.book.domain.entity.BookcrewEntity.Role;
import movlit.be.book.domain.entity.GenerateUUID;
import movlit.be.book.getBookApi.BookCategory;
import movlit.be.book.getBookApi.dto.BookBestResponseDto;
import movlit.be.book.getBookApi.dto.BookBestResponseDto.Item;
import movlit.be.book.infra.persistence.jpa.BookBestsellerJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookGenreJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookRCrewJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookcrewJpaRepository;
import movlit.be.common.util.ids.BookBestsellerId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.BookRCrewId;
import movlit.be.common.util.ids.BookcrewId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:application-test.properties")
public class BookESBestsellerService {

    private final BookESRepository bookESRepository;

    private final RestTemplate restTemplate;
//        private final BookJpaRepository bookRepository;
//        private final BookcrewJpaRepository bookcrewRepository ;
//        private final BookBestsellerJpaRepository bookBestsellerRepository;
//        private final BookRCrewJpaRepository bookRCrewRepository;
//
//        private final BookGenreJpaRepository bookGenreJpaRepository;

    // 테스트url
    private static final String baseUrl = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?";
    public BookES savedEsBook;

    @Value("${aladin.key}")
    String apiKey;

    public void repeatGet(int times) {
        for (int i = 1; i < times + 1; i++) {

            String url = baseUrl + "ttbkey=" + apiKey +
                    "&QueryType=Bestseller&MaxResults=50&start=" + i
                    + "&SearchTarget=Book&Cover=Big&output=js&Version=20131101";

            insertBook(url);
        }
    }

    public void insertBook(String url) {

        BookBestResponseDto bookResponseDto = restTemplate.getForObject(url, BookBestResponseDto.class);

        if (bookResponseDto != null) {

            List<Item> bookList = bookResponseDto.getItem();

            if (bookList == null) {
                System.out.println("booklist는 null이다");
            }
            if (bookList != null) {

                System.out.println("책 목록이 존재함, 책 수: " + bookList.size());

                // booklist 순회하며 bookEntity 저장
                for (Item book : bookList) {
                    try {
                        savedEsBook = null;

                        String categoryName = book.getCategoryName();

                        if (!categoryName.contains("국내도서>외국어>") && !categoryName.contains("국내도서>수험서/자격증")) {

                            //BookId bookId = new BookId(book.getIsbn13());

                            Long bookId = Long.parseLong(book.getIsbn13());

                            // 중복데이터 체크
                            if (!bookESRepository.existsById(bookId)) {

                                BookES savedBook = BookES.builder()
                                        .bookId(bookId)
                                        //.bookId(new BookId(book.getIsbn13())) // bookId
                                        .isbn(book.getIsbn())
                                        .title(book.getTitle())
                                        .crew(book.getAuthor())
                                        .publisher(book.getPublisher())
                                        .pubDate(LocalDate.parse(book.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                                .atStartOfDay())
                                        .description(book.getDescription())
                                        .categoryId(book.getCategoryId()) // test용
                                        .categoryName(book.getCategoryName())
                                        .bookImgUrl(book.getCover().replace("cover200", "cover500"))
                                        .build();

                                // BookEntity 먼저 저장 - savedBestSeller 값 설정을 위해
                                savedEsBook = bookESRepository.save(savedBook);


                            } else {
                                System.out.println("이미 존재하는 책 :: " + book.getIsbn13());
                            }

                        }
                    } catch (Exception e) {
                        System.err.println("Error processing book: " + savedEsBook.getTitle() + ", " + e.getMessage());
                    }
                }

            }
        }


    }


}
