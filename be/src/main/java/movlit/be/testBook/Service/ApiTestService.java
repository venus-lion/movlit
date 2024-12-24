package movlit.be.testBook.Service;

import java.awt.print.Book;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;

import movlit.be.testBook.Dto.BookResponseDto;
import movlit.be.testBook.Dto.BookResponseDto.Item;
import movlit.be.testBook.Entity.BookBestsellerEntity;
import movlit.be.testBook.Entity.BookEntity;
import movlit.be.testBook.Entity.BookRCrewEntity;
import movlit.be.testBook.Entity.BookcrewEntity;
import movlit.be.testBook.Entity.BookcrewEntity.Role;
import movlit.be.testBook.Repository.BookBestsellerRepository;
import movlit.be.testBook.Repository.BookRCrewRepository;
import movlit.be.testBook.Repository.BookRepository;
import movlit.be.testBook.Repository.BookcrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:application-test.properties")
public class ApiTestService {

    private final RestTemplate restTemplate;
    private final BookRepository bookRepository;

    // 테스트url
    private static final String baseUrl = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?";

    @Value("${aladin.key}")
    String apiKey = "ttbolivia09291715001";

    // data 5개만 가져오게 수정 !
    String url = baseUrl + "ttbkey=" + apiKey +
            "&QueryType=Bestseller&MaxResults=10&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101";

    public void saveBookDatafromApi(){
        BookResponseDto bookResponseDto = restTemplate.getForObject(url, BookResponseDto.class);

        if (bookResponseDto != null && bookResponseDto.getItem() != null){
            List<Item> bookList = bookResponseDto.getItem();

            for (Item book : bookList){
                saveBookInformationAll(book); // book 엔디티, bookcrew 엔디티, bestseller 모두 저장
            }

        }

    }

    public void saveBookInformationAll(Item book){
        if (!bookRepository.findById(book.getIsbn()).isPresent()) { // DB에 bookEntity 이미 저장되어 있는지, 중복 검사
            BookEntity bookEntity = BookEntity.builder()
                    .bookId(book.getIsbn())
                    .title(book.getTitle())
                    .publisher(book.getPublisher())
                    .pubDate(
                            LocalDate.parse(book.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                    .atStartOfDay())
                    .description(book.getDescription())
                    .bookImgUrl(book.getCover().replace("cover200", "cover500"))
                    .stockStatus(book.getStockStatus() == null ? "in-Stock" : book.getStockStatus())
                    .mallUrl(book.getLink())
                    .build();

            bookRepository.save(bookEntity); // bookEntity 저장



        } else {
            System.out.println("bookentity가 이미 데이터베이스에 저장되어 있습니다.");
        }

    }



}
