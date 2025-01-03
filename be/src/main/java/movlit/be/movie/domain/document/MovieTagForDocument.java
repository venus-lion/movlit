package movlit.be.movie.domain.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieTagForDocument {

    @Field(type = FieldType.Keyword)
    private Long movieTagId; // 태그 ID

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "ko", type = FieldType.Text, analyzer = "korean_analyzer")
            })
    private String tagName; // 태그 이름

}
