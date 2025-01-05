package movlit.be.bookES;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.common.util.ids.BookId;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "book")
@Setting(settingPath = "/mappings/book-setting.json")
@Mapping(mappingPath = "/mappings/book-mapping.json")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookES {


    @Id
    private String bookId; // isbn13, uuid

    @Field(type = FieldType.Keyword)  // 필드 타입을 'Keyword'로 지정
    private String isbn;  // isbn

    @Field(type = FieldType.Text)  // 텍스트로 처리, 전체 텍스트 검색 가능
    private String title;

    @Field(type = FieldType.Text)  // 텍스트로 처리, 전체 텍스트 검색 가능
    private List<String> crew; // 작가, 편집자, 기타..

    @Field(type = FieldType.Text)  // 텍스트로 처리
    private String publisher;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime pubDate;

    @Field(type = FieldType.Text)  // 긴 설명을 텍스트로 처리
    private String description;

    @Field(type = FieldType.Text)  // 카테고리 이름
    private String categoryName;

    @Field(type = FieldType.Keyword)  // 책 이미지 URL
    private String bookImgUrl;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    // @Field(type = FieldType.Date)  // 생성일자
    private LocalDateTime regDt;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    // @Field(type = FieldType.Date)  // 수정일자
    private LocalDateTime updDt;

}
