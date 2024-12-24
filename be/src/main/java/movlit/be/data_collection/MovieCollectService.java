package movlit.be.data_collection;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import movlit.be.Movie;
import movlit.be.MovieCrew;
import movlit.be.MovieRole;
import movlit.be.MovieTag;
import movlit.be.MovieTagId;
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

    @Autowired
    private MovieTagRepository movieTagRepository;

    @Autowired
    private MovieCrewRepository movieCrewRepository;

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
        String sortBy = "&sort_by=primary_release_date.desc";

        // 개봉일자 between (YYYY-MM-DD)        String dateGte = "2024-12-24";
        LocalDate today = LocalDate.now();
        String releaseDateGte = "&release_date.gte=2023-01-01";
        String releaseDateLte = "&release_date.lte=2024-12-31";
        // 데이터 수집 주기: 1주 -> SYSDATE(수집 날짜) -> date계산 1주일 전?

        String apiUrl = "https://api.themoviedb.org/3/discover/movie?api_key=" + key + "&page=" + page
                + language + region + includeAdult + releaseDateGte + releaseDateLte + sortBy;
        Map<String, Object> discoverList = restTemplate.getForObject(apiUrl, Map.class);

        // discover List에서 id당 detail 뽑아오기
        if (ObjectUtils.isEmpty(discoverList.get("results"))) {
            return List.of();
        }

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
            if (releaseDate.isAfter(today)) {
                continue;
            }

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

            log.info("page={}", page);
            log.info("id={}", id);
        }

        movieCollectRepository.saveAll(movieList);

        return movieList;
    }

    /*
    Move Tag(Keyword) 수집
     */
    public List<MovieTag> getMovieTagList(Movie movie){

        List<MovieTag> movieTagList = new ArrayList<>();

        String apiUrl = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/keywords?api_key=" + key;
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);


        if(ObjectUtils.isEmpty(response.get("keywords"))){
            return List.of();
        }

        List<Map<String, Object>> keywordList = (List<Map<String, Object>>) response.get("keywords");

        for(Map<String, Object> keyword : keywordList){
            Long id = Long.valueOf((Integer) keyword.get("id"));
            String name = (String) keyword.get("name");
            MovieTagId movieTagId = MovieTagId.builder()
                    .movieId(movie.getId()).movieTagId(id)
                    .build();
            MovieTag movieTag = MovieTag.builder()
                    .movieTagId(movieTagId)
                    .name(name)
                    .movie(movie)
                    .regDt(LocalDateTime.now())
                    .updDt(LocalDateTime.now())
                    .delYn(false)
                    .build();

            movieTagList.add(movieTag);
        }



        movieTagRepository.saveAll(movieTagList);

        return movieTagList;
    }

    public List<Movie> getAllMovieList(){
        return movieCollectRepository.findAll();
    }
    public List<MovieCrew> getMovieCrewList() {
        List<MovieCrew> resultList = new ArrayList<>();

        // 언어, 지역
        String language = "&language=ko";
        List<Movie> all = movieCollectRepository.findAll();

        all.forEach(movie -> {
            String apiUrl =
                    "https://api.themoviedb.org/3/movie/" + movie.getId() + "/credits" + "?api_key=" + key
                            + language;
            Map<String, Object> movieCrewInfo = restTemplate.getForObject(apiUrl, Map.class);

            // MovieCrew
            /**
             *     private Long id;
             *     private String name;
             *     @Enumerated(EnumType.STRING)
             *     private MovieRole role;
             *     private String charName;
             *     private String profileImgUrl;
             *     private int orderNo; // 감독: -1, 배우: 0부터 정렬
             */

            // 1. Cast -> Order로 정렬
            List<Map<String, Object>> castList = (List<Map<String, Object>>) movieCrewInfo.get("cast");
            castList.forEach(cast -> {
                Long id = Long.valueOf((Integer) cast.get("id"));
                String name = (String) cast.get("name");
                MovieRole role = MovieRole.CAST;
                String charName = (String) cast.get("character");
                String profileImgUrl = (String) cast.get("profile_path");
                int orderNo = (Integer) cast.get("order");

                MovieCrew movieCrew = MovieCrew.builder()
                        .id(id)
                        .name(name)
                        .role(role)
                        .charName(charName)
                        .profileImgUrl(profileImgUrl)
                        .orderNo(orderNo)
                        .build();

                resultList.add(movieCrew);
            });


            // 2. Crew (DIRECTOR) -> 0번째 배열, job = "Director"
            List<Map<String, Object>> crewList = (List<Map<String, Object>>) movieCrewInfo.get("crew");
            if (crewList.isEmpty()) {
                return;
            }
            Map<String, Object> crew = crewList.get(0);
            Long id = Long.valueOf((Integer) crew.get("id"));
            String name = (String) crew.get("name");
            MovieRole role = MovieRole.DIRECTOR;
            String charName = (String) crew.get("character");
            String profileImgUrl = (String) crew.get("profile_path");
            int orderNo = -1;

            MovieCrew movieCrew = MovieCrew.builder()
                    .id(id)
                    .name(name)
                    .role(role)
                    .charName(charName)
                    .profileImgUrl(profileImgUrl)
                    .orderNo(orderNo)
                    .build();

            resultList.add(movieCrew);

        });

        movieCrewRepository.saveAll(resultList);

        return resultList;
    }


}
