package movlit.be.testBook.Service;

import java.awt.print.Book;
import java.net.URI;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
@PropertySource("classpath:application-test.properties")
public class ApiTestService {

    private final RestTemplate restTemplate;

    // 테스트url
    private static final String baseUrl = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?";

    String apiKey = "ttbolivia09291715001";

    String url = baseUrl + "ttbkey=" + apiKey +
            "&QueryType=Bestseller&MaxResults=50&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101";

//    URI url2 = URI.create(url);

    public void testapiBook() {

        BookResponseDto bookResponseDto = restTemplate.getForObject(url, BookResponseDto.class);

        if (bookResponseDto != null){
            List<Item> bookList = bookResponseDto.getItem();

            if (bookList != null) {
                for (Item book : bookList) {
                    System.out.println("book - title : " + book.getTitle());
                }
            } else {
                System.out.println("booklist is null");
            }
        }

    }

}
