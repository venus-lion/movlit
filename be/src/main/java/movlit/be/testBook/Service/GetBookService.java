package movlit.be.testBook.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import movlit.be.testBook.Config.AladinConfig;
import movlit.be.testBook.Dto.BookResponseDto;
import movlit.be.testBook.Entity.BookBestsellerEntity;
import movlit.be.testBook.Entity.BookEntity;
import movlit.be.testBook.Entity.BookcrewEntity;
import movlit.be.testBook.Entity.BookcrewEntity.Role;
import movlit.be.testBook.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetBookService {
    private RestTemplate restTemplate;
    //private final String BASE_URL = "https://www.aladin.co.kr/ttb/api/ItemList.aspx";
    @Autowired
    private BookRepository bookRepository;

    // 테스트url
    String url = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?ttbkey=ttbhgw0014681535001&QueryType=Bestseller&MaxResults=50&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101;"


    public void insertBook(BookEntity bookEntity){
        // API 데이터 -> ResponseEntity로 처리
        ResponseEntity<List<BookResponseDto>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<BookResponseDto>>() {
                }
        );

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            // 북 리스트 출력 및 저장
            List<BookResponseDto> bookList = responseEntity.getBody();
            if(bookList != null){
                // 북 리스트 저장
                for(BookResponseDto book : bookList){
                    System.out.println("////////////// 책 \n" + book);
                    BookEntity savedBook = BookEntity.builder()
                            .bookId(book.getIsbn())
                            .title(book.getTitle())
                            .publisher(book.getPublisher())
                            .pubDate(LocalDateTime.parse(book.getPubDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .description(book.getDescription())
                            .bookImgUrl(book.getCover().replace("cover200", "cover500"))
                            .stockStatus(book.getStockStatus()==null? "in-Stock":book.getStockStatus())
                            .regDt(LocalDateTime.now())
                            .updDt(LocalDateTime.now())
                            .build();

                    // BookEntity 먼저 저장 - savedBestSeller 값 설정을 위해
                    BookEntity savedBookEntity = bookRepository.save(savedBook); 


                    String[] crewArr = book.getAuthor().split(", ");

                    
                    int crewNum = crewArr.length;
                    for(int i=0; i  <crewNum)



                    BookcrewEntity savedCrew = BookcrewEntity.builder()
                            .name(book.getAuthor())
                            .role()
                            .build();

                    BookBestsellerEntity savedBestSeller = BookBestsellerEntity.builder()
                                    .book(savedBookEntity) // FK - BOOKEntity
                                    .bestRank(book.getBestRank())
                                    .bestDuration(book.getBestDuration())
                                    .build();

                    bookRepository.save(book);


                }
            }
        }
        else{
            System.out.println("책 api 호출 실패 : " + responseEntity.getStatusCode());
        }


    }


    // 예시: 파싱된 role 값을 설정하는 메서드
    public void setParsedRole(String roleString) {
        Role role;
        enum Role {
            AUTHOR, // 작가
            TRANSLATOR, // 옮긴이
            EDITOR // 지은이
        }

        switch (roleString) {
            case "작가":
                role = Role.AUTHOR;
                break;
            case "옮긴이":
                role = Role.TRANSLATOR;
                break;
            case "지은이":
                role = Role.EDITOR;
                break;
            // ... 나머지 role에 대한 파싱 로직 ...
            default:
                // 파싱할 수 없는 경우 예외 처리 또는 기본값 설정
                role = null; // 또는 다른 기본 Role 값
                break;
        }
    }


}
