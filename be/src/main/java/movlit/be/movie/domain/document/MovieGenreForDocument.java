package movlit.be.movie.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieGenreForDocument {

    @Field(type = FieldType.Keyword)
    private Long genreId; // 장르 ID

    @Field(type = FieldType.Text)
    private String genreName; // 장르 이름

}
