package movlit.be.movie.domain.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "movies")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MovieDocument {

    @Id
    private Long movieId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String originalTitle;

    @Field(type = FieldType.Text)
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

    @Field(type = FieldType.Text)
    private String tagline;

    @Field(type = FieldType.Boolean)
    private boolean delYn;

    @Field(type = FieldType.Long)
    private Long heartCount;

    @Field(type = FieldType.Nested)
    private List<MovieGenreForDocument> movieGenre = new ArrayList<>();

//    @Field(type = FieldType.Nested)
//    private List<MovieRCrewEntity> movieRCrewEntityList = new ArrayList<>();
//
    @Field(type = FieldType.Nested)
    private List<MovieTagForDocument> movieTag = new ArrayList<>();

}
