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
public class MovieTagForDocument {

    @Field(type = FieldType.Keyword)
    private Long movieTagId; // 태그 ID

    @Field(type = FieldType.Text)
    private String tagName; // 태그 이름

}
