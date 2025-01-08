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
public class MovieCrewForDocument {

    @Field(type = FieldType.Keyword)
    private String movieCrewId;

    @MultiField(mainField = @Field(type = FieldType.Keyword),
            otherFields = {
                    @InnerField(suffix = "ko", type = FieldType.Text, analyzer = "korean_analyzer", searchAnalyzer = "korean_analyzer"),
                    @InnerField(suffix = "en", type = FieldType.Text, analyzer = "english_analyzer", searchAnalyzer = "english_analyzer"),
                    @InnerField(suffix = "ngram", type = FieldType.Text, analyzer = "my_ngram_analyzer", searchAnalyzer = "my_ngram_analyzer"),
                    @InnerField(suffix = "standard", type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
            })
    private String name;

    @Field(type = FieldType.Keyword)
    private String role;

    @Field(type = FieldType.Keyword)
    private String charName;

    @Field(type = FieldType.Integer)
    private int orderNo;

}
