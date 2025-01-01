package movlit.be.book.getBookApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.getBookApi.BookResponseDto.Item;
import movlit.be.book.domain.entity.BookBestsellerEntity;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookRCrewEntity;
import movlit.be.book.domain.entity.BookcrewEntity;
import movlit.be.book.domain.entity.GenerateUUID;
import movlit.be.book.infra.persistence.jpa.BookBestsellerJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookRCrewJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookJpaRepository;
import movlit.be.book.infra.persistence.jpa.BookcrewJpaRepository;
import movlit.be.common.util.ids.BookBestsellerId;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.BookRCrewId;
import movlit.be.common.util.ids.BookcrewId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import movlit.be.book.domain.entity.BookcrewEntity.Role;
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-test.properties")
public class GetBookService {
    private final RestTemplate restTemplate;
    private final BookJpaRepository bookRepository;
    private final BookcrewJpaRepository bookcrewRepository ;
    private final BookBestsellerJpaRepository bookBestsellerRepository;
    private final BookRCrewJpaRepository bookRCrewRepository;

    // 테스트url
    private static final String baseUrl = "https://www.aladin.co.kr/ttb/api/ItemList.aspx?";

    @Value("${aladin.key}")
    String apiKey;

    public void repeatGet(int times){
        for(int i=1; i < times+1; i++){

            String url = baseUrl + "ttbkey=" + apiKey +
                    "&QueryType=Bestseller&MaxResults=50&start="+i+"&SearchTarget=Book&Cover=Big&output=js&Version=20131101";

            insertBook(url);
        }
    }

    public void insertBook(String url){

        BookResponseDto bookResponseDto = restTemplate.getForObject(url, BookResponseDto.class);

        if(bookResponseDto != null ){

            List<Item> bookList = bookResponseDto.getItem();

            if (bookList == null){
                System.out.println("booklist는 null이다");
            }
            if(bookList != null){

                System.out.println("책 목록이 존재함, 책 수: " + bookList.size());

                // booklist 순회하며 bookEntity 저장
                for(Item book : bookList) {
                    try {

                        String categoryName = book.getCategoryName();

                        if(!categoryName.contains("국내도서>외국어>") && !categoryName.contains("국내도서>수험서/자격증")) {

                            BookEntity savedBook = BookEntity.builder()
                                    .bookId(new BookId(book.getIsbn13())) // bookId
                                    .isbn(book.getIsbn())
                                    .title(book.getTitle())
                                    .publisher(book.getPublisher())
                                    .pubDate(LocalDate.parse(book.getPubDate(), DateTimeFormatter.ISO_LOCAL_DATE)
                                            .atStartOfDay())
                                    .description(book.getDescription())
                                    .categoryName(book.getCategoryName())
                                    .bookImgUrl(book.getCover().replace("cover200", "cover500"))
                                    .stockStatus(book.getStockStatus().length() == 0 ? "판매중"
                                            : book.getStockStatus()) // 상품재고가 null이면 재고있음
                                    .mallUrl(book.getLink())
                                    .build();

                            // BookEntity 먼저 저장 - savedBestSeller 값 설정을 위해
                            BookEntity savedBookEntity = bookRepository.save(savedBook);


                            String[] crewArr = book.getAuthor().split(", ");

                            int crewNum = crewArr.length;
                            for (int i = 0; i < crewNum; i++) {
                                String input = crewArr[i];

                                // 정규표현식 -> "한강 (지은이)" : 괄호밖 -> 이름, 공백+괄호안 -> 역할
                                String regex = "(.+?)(?:\\s\\((.*?)\\))?$";
                                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
                                java.util.regex.Matcher matcher = pattern.matcher(input);

                                if (matcher.matches()) {
                                    String name = matcher.group(1).trim();

                                    Role role = setParsedRole(matcher.group(2) != null ? matcher.group(2).trim() : "기타");




                                    BookcrewEntity savedBookcrew = BookcrewEntity.builder()
                                            .crewId(new BookcrewId(GenerateUUID.generateUUID()))
                                            .name(name)
                                            .role(role)
                                            .profileImageUrl("/book_crew_profile/default_profile.png")
                                            .build();


                                    BookcrewEntity savedBookcrewEntity = bookcrewRepository.save(savedBookcrew);

                                    BookRCrewEntity savedBookRCrewEntity = BookRCrewEntity.builder()
                                            .bookRCrewId(new BookRCrewId(GenerateUUID.generateUUID()))
                                            .bookEntity(savedBookEntity)
                                            .bookcrewEntity(savedBookcrewEntity)
                                            .build();

                                    bookRCrewRepository.save(savedBookRCrewEntity);




                                } else {
                                    System.out.println(book.getIsbn() + " " + book.getTitle() + " " +
                                            crewArr[i] + "패턴이 불일치, 크루 저장 실패");
                                }

                            }


                            BookBestsellerEntity savedBestSeller = BookBestsellerEntity.builder()
                                    .bookBestsellerId(new BookBestsellerId(GenerateUUID.generateUUID()))
                                    .book(savedBookEntity) // FK - BOOKEntity
                                    .bestRank(book.getBestRank())
                                    .bestDuration(book.getBestDuration())
                                    .build();

                            bookBestsellerRepository.save(savedBestSeller);
                        }
                    } catch (Exception e){
                        System.err.println("Error processing book: " + book.getTitle() + ", " + e.getMessage());
                    }
                }

            }
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
