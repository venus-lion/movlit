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
import movlit.be.testBook.Repository.BookBestsellerRepository;
import movlit.be.testBook.Repository.BookRepository;
import movlit.be.testBook.Repository.BookcrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:application-test.properties")
public class GetBookService {
    private RestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookcrewRepository bookcrewRepository ;
    @Autowired
    private BookBestsellerRepository bookBestsellerRepository;

    // 테스트url
    String baseUrl = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?";

    @Value("${aladin.key}")
    String apiKey;
    String url = baseUrl + "ttbkey=" + apiKey +
            "&QueryType=Bestseller&MaxResults=50&start=1&SearchTarget=Book&Cover=Big&output=js&Version=20131101";


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
                    for(int i=0; i < crewNum; i++){
                        String input = crewArr[i];

                        String regex = "^(.*?)(?:\\\\s*\\\\((.*?)\\\\))?$";
                        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                        java.util.regex.Matcher matcher = pattern.matcher(input);

                        if (matcher.matches()) {
                            String name = matcher.group(1).trim();
                            Role role = setParsedRole(matcher.group(2) != null ? matcher.group(2).trim() : "기타");

                            BookcrewEntity savedBookcrew = BookcrewEntity.builder()
                                    .name(name)
                                    .role(role)
                                    .build();

                            bookcrewRepository.save(savedBookcrew);


                        } else {
                            System.out.println( book.getIsbn() +  " " + book.getTitle() +  " " +
                                    crewArr[i]  + "패턴이 불일치, 크루 저장 실패");
                        }
                    }



                    BookBestsellerEntity savedBestSeller = BookBestsellerEntity.builder()
                                    .book(savedBookEntity) // FK - BOOKEntity
                                    .bestRank(book.getBestRank())
                                    .bestDuration(book.getBestDuration())
                                    .build();

                    bookBestsellerRepository.save(savedBestSeller);




                }
            }
        }
        else{
            System.out.println("책 api 호출 실패 : " + responseEntity.getStatusCode());
        }


    }


    // 예시: 파싱된 role 값을 설정하는 메서드
    public Role setParsedRole(String roleString) {
        Role role;

        switch (roleString) {
            case "지은이":
                role = Role.AUTHOR;
                break;
            case "옮긴이":
                role = Role.TRANSLATOR;
                break;
            // ... 나머지 role에 대한 파싱 로직 ...
            default:
                // 파싱할 수 없는 경우 예외 처리 또는 기본값 설정
                role = Role.UNKNOWN; // 또는 다른 기본 Role 값 반환
                break;
        }

        return role; // 역할 반환
    }


}
