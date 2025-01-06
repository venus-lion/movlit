package movlit.be.movie.domain.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import movlit.be.movie.domain.entity.MovieRCrewEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = "movies")
@Setting(settingPath = "/mappings/movie-setting.json")
@Mapping(mappingPath = "/mappings/movie-mapping.json")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDocument {

    @Id
    private Long movieId;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "ko", type = FieldType.Text, analyzer = "korean_analyzer"),
                    @InnerField(suffix = "ngram", type = FieldType.Text, analyzer = "my_ngram_analyzer")
            })
    private String title;

    @Field(type = FieldType.Text)
    private String originalTitle;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "ko", type = FieldType.Text, analyzer = "korean_analyzer"),
                    @InnerField(suffix = "ngram", type = FieldType.Text, analyzer = "my_ngram_analyzer")
            })
    private String overview;

    @Field(type = FieldType.Double)
    private Double popularity;

    @Field(type = FieldType.Text)
    private String posterPath;

    @Field(type = FieldType.Text)
    private String backdropPath;

    @Field(type = FieldType.Date, format = DateFormat.date, pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Field(type = FieldType.Keyword)
    private String originalLanguage;

    @Field(type = FieldType.Long)
    private Long voteCount;

    @Field(type = FieldType.Double)
    private Double voteAverage;

    // Detail Fields
    @Field(type = FieldType.Keyword)
    private String productionCountry;

    @Field(type = FieldType.Integer)
    private Integer runtime;

    @Field(type = FieldType.Keyword)
    private String status;

    @MultiField(mainField = @Field(type = FieldType.Text, analyzer = "english_analyzer"),
            otherFields = {
                    @InnerField(suffix = "ko", type = FieldType.Text, analyzer = "korean_analyzer"),
                    @InnerField(suffix = "ngram", type = FieldType.Text, analyzer = "my_ngram_analyzer")
            })
    private String tagline;

    @Field(type = FieldType.Boolean)
    private boolean delYn;

    @Field(type = FieldType.Nested)
    private List<MovieGenreForDocument> movieGenre = new ArrayList<>();

//    @Field(type = FieldType.Nested)
//    private List<MovieRCrewEntity> movieRCrewEntityList = new ArrayList<>();

    @Field(type = FieldType.Nested)
    private List<MovieTagForDocument> movieTag = new ArrayList<>();

}
