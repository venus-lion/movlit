package movlit.be.data_collection;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import movlit.be.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@PropertySource("classpath:application.properties")
public class MovieCollectService {

    @Value("${tmdb.key}")
    String key;

    @Value("${tmdb.accessToken}")
    String accessToken;

    private final RestTemplate restTemplate;

    @Autowired
    private MovieCollectRepository movieCollectRepository;

    public MovieCollectService(RestTemplateBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + accessToken);
        restTemplate = builder.build();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public List<Movie> getMovieDiscoverView(String page) {
        // 언어, 지역
        String language = "&language=ko";
        String region = "&region=KR";

        String includeAdult = "&include_adult=false";
        String sortBy = "&sort_by=popularity.desc";

        // 개봉일자 between (YYYY-MM-DD)
//        String release_date_gte = "&release_date.gte=";
//        String release_date_lte = "&release_date.lte=";

        ObjectMapper objectMapper = new ObjectMapper();

        String apiUrl = "https://api.themoviedb.org/3/discover/movie?api_key=" + key + "&page=" + page
                + language + region + includeAdult + includeAdult + sortBy;
        Map<String, Object> discoverList = restTemplate.getForObject(apiUrl, Map.class);

        // discover List에서 id당 detail 뽑아오기
        List<Map<String, Object>> resultList = (List<Map<String, Object>>) discoverList.get("results");

        List<Movie> movieList = new ArrayList<>();

        for (Map<String, Object> result : resultList) {
            String apiId = Integer.toString((Integer) result.get("id"));
            String detailApiUrl = "https://api.themoviedb.org/3/movie/" + apiId + "?api_key=" + key + "&language=ko-KR";
            Map<String, Object> detailResult = restTemplate.getForObject(detailApiUrl, Map.class);

            // entity 빌드
            // discover
            /*
            *
            private String title;
            private String originalTitle;
            private String overview;
            private Double popularity;
            private String posterPath;
            private String backdropPath;
            private LocalDate releaseDate;
            private String originalLanguage;
            private Long voteCount;
            private Double voteAverage;
            *
            * */
            Integer id = (Integer) result.get("id");
            String title = (String) result.get("title");
            String originalTitle = (String) result.get("original_title");
            String overview = (String) result.get("overview");
            Double popularity = (Double) result.get("popularity");
            String posterPath = (String) result.get("poster_path");
            String backdropPath = (String) result.get("backdrop_path");
            String releaseDateStr = (String) result.get("release_date");
            LocalDate releaseDate = LocalDate.parse(releaseDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            String originalLanguage = (String) result.get("original_language");
            Long voteCount = Long.valueOf((Integer) result.get("vote_count"));
            Double voteAverage = (Double) result.get("vote_average");
            // detail
            /*
            * private String productionCountry;
            private Integer runtime;
            private String status;
            private String tagline;
            * */
            String productionCountry;
            if (ObjectUtils.isEmpty(detailResult.get("production_countries"))) {
                productionCountry = "NONE";
            } else {
                List<Object> productionCountries = (List<Object>) detailResult.get("production_countries");
                productionCountry = (String) ((Map<String, Object>) productionCountries.get(0)).get("iso_3166_1");
            }
            Integer runtime = (Integer) detailResult.get("runtime");
            String status = (String) detailResult.get("status");
            String tagline = (String) detailResult.get("tagline");

            // Custom
            /**
             *     private LocalDateTime regDt;
             *     private LocalDateTime updDt;
             *     private boolean delYn;
             *     private Long heartCount;
             */

            Movie movie = Movie.builder()
                    .id(Long.valueOf(id))
                    .title(title)
                    .originalTitle(originalTitle)
                    .overview(overview)
                    .popularity(popularity)
                    .posterPath(posterPath)
                    .backdropPath(backdropPath)
                    .releaseDate(releaseDate)
                    .originalLanguage(originalLanguage)
                    .voteCount(voteCount)
                    .voteAverage(voteAverage)
                    .productionCountry(productionCountry)
                    .runtime(runtime)
                    .status(status)
                    .tagline(tagline)
                    .heartCount(0L)
                    .regDt(LocalDateTime.now())
                    .updDt(LocalDateTime.now())
                    .delYn(false)
                    .build();

            movieList.add(movie);
        }

        movieCollectRepository.saveAll(movieList);
        return movieList;

    }

}
