package movlit.be.data_collection.movie;

import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import movlit.be.common.exception.MovieNotFoundException;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MovieCrewId;
import movlit.be.data_collection.MovieGenreCollectRepository;
import movlit.be.data_collection.movie.jpa.MovieCollectRepository;
import movlit.be.data_collection.movie.jpa.MovieTagRepository;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.domain.MovieRole;
import movlit.be.movie.domain.entity.MovieCrewEntity;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.domain.entity.MovieGenreEntity;
import movlit.be.movie.domain.entity.MovieGenreIdForEntity;
import movlit.be.movie.domain.entity.MovieRCrewEntity;
import movlit.be.movie.domain.entity.MovieRCrewIdForEntity;
import movlit.be.movie.domain.entity.MovieTagEntity;
import movlit.be.movie.domain.entity.MovieTagIdForEntity;
import movlit.be.movie.infra.persistence.jpa.MovieCrewJpaRepository;
import movlit.be.movie.infra.persistence.jpa.MovieRCrewJpaRepository;
import movlit.be.movie_heart_count.application.service.MovieHeartCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@Transactional
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
    private MovieGenreCollectRepository movieGenreCollectRepository;

    @Autowired
    private MovieCrewJpaRepository movieCrewJpaRepository;

    @Autowired
    private MovieRCrewJpaRepository movieRCrewJpaRepository;

    @Autowired
    private MovieHeartCountService movieHeartCountService;

    public MovieCollectService(RestTemplateBuilder builder) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", "Bearer " + accessToken);
        restTemplate = builder.build();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    public List<MovieEntity> getMovieDiscoverView(String page) {
        // 언어, 지역
        String language = "&language=ko";
        String region = "&region=KR";

        String includeAdult = "&include_adult=false";
        String sortBy = "&sort_by=popularity.desc";

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
        List<MovieEntity> movieList = new ArrayList<>();

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

            String posterPath = Optional.ofNullable((String) result.get("poster_path")).orElse("");
            if (!posterPath.isEmpty()) {
                posterPath = "http://image.tmdb.org/t/p/original" + posterPath;
            }

            String backdropPath = Optional.ofNullable((String) result.get("backdrop_path")).orElse("");
            if (!backdropPath.isEmpty()) {
                backdropPath = "http://image.tmdb.org/t/p/original" + backdropPath;
            }

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

            MovieEntity movie = MovieEntity.builder()
                    .movieId(Long.valueOf(id))
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
                    .regDt(LocalDateTime.now())
                    .updDt(LocalDateTime.now())
                    .delYn(false)
                    .build();

            // heartCount 처리
            movieHeartCountService.save(MovieConvertor.toMovieHeartCountEntity(movie.getMovieId()));
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
    public List<MovieTagEntity> getMovieTagList(MovieEntity movie) {

        List<MovieTagEntity> movieTagList = new ArrayList<>();

        String apiUrl = "https://api.themoviedb.org/3/movie/" + movie.getMovieId() + "/keywords?api_key=" + key;
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
        log.info("=== response : {}", response);
        if (ObjectUtils.isEmpty(response.get("keywords"))) {
            return List.of();
        }

        List<Map<String, Object>> keywordList = (List<Map<String, Object>>) response.get("keywords");
        keywordList.forEach(keyword -> {
            Long id = Long.valueOf((Integer) keyword.get("id"));
            String name = (String) keyword.get("name");
            MovieTagIdForEntity movieTagId = new MovieTagIdForEntity(id, movie.getMovieId());

            MovieTagEntity movieTag = MovieTagEntity.builder()
                    .movieTagIdForEntity(movieTagId)
                    .name(name)
                    .movieEntity(movie)
                    .regDt(LocalDateTime.now())
                    .updDt(LocalDateTime.now())
                    .delYn(false)
                    .build();

            movieTagList.add(movieTag);
        });

//        for (Map<String, Object> keyword : keywordList) {
//            Long id = Long.valueOf((Integer) keyword.get("id"));
//            String name = (String) keyword.get("name");
//            MovieTagIdForEntity movieTagId = new MovieTagIdForEntity(id, movie.getMovieId());
//
//            MovieTagEntity movieTag = MovieTagEntity.builder()
//                    .movieTagIdForEntity(movieTagId)
//                    .name(name)
//                    .movieEntity(movie)
//                    .regDt(LocalDateTime.now())
//                    .updDt(LocalDateTime.now())
//                    .delYn(false)
//                    .build();
//
//            movieTagList.add(movieTag);
//        }

        movieTagRepository.saveAll(movieTagList);

        return movieTagList;
    }

    public List<MovieEntity> getAllMovieList() {
        return movieCollectRepository.findAll();
    }

    public List<MovieGenreEntity> getMovieGenreList(MovieEntity movie) {
        List<MovieGenreEntity> genreList = new ArrayList<>();
        Set<MovieGenreIdForEntity> genreIdSet = new LinkedHashSet<>();

        String detailApiUrl =
                "https://api.themoviedb.org/3/movie/" + movie.getMovieId() + "?api_key=" + key + "&language=ko-KR";
        Map<String, Object> response = restTemplate.getForObject(detailApiUrl, Map.class);
        Optional<List<Map<String, Object>>> genres = Optional.ofNullable(
                (List<Map<String, Object>>) response.get("genres"));

        // API 장르ID -> 서비스 장르ID 매핑
        genres.ifPresent(list -> list.forEach(genre -> {
            Integer apiGenreId = (Integer) genre.get("id");
            Long genreId = this.mappingGenreEntityFromApiGenreId(apiGenreId, movie);
            genreIdSet.add(new MovieGenreIdForEntity(movie.getMovieId(), genreId));
        }));
        log.info("==== genreIdSet : {}", genreIdSet);

        genreIdSet.forEach(id -> {
            MovieGenreEntity movieGenreEntity = new MovieGenreEntity(id, movie);
            genreList.add(movieGenreEntity);
        });

        movieGenreCollectRepository.saveAll(genreList);
        return genreList;
    }

    private Long mappingGenreEntityFromApiGenreId(int apiGenreId, MovieEntity movie) {
        return switch (apiGenreId) {
//            case 28, 12 ->    // 액션(28), 모험(12)
//                    new MovieGenreEntity(
//                            new MovieGenreIdForEntity(1L, movie.getMovieId())
//                            , movie);
            case 28, 12 -> 1L;    // 액션(28), 모험(12)
            case 16 -> 2L;        // 애니매이션(16)
            case 35 -> 3L;       // 코미디(35)
            case 80 -> 4L;        // 범죄(80)
            case 99 -> 5L;        // 다큐멘터리(99)
            case 18, 10751 -> 6L;       // 드라마(18), 가족(10751)
            case 14 -> 7L;       // 판타지(14)
            case 36 -> 8L;       // 역사(36)
            case 10402 -> 9L;       // 음악(10402)
            case 9648 -> 10L;       // 미스테리(9648)
            case 10749 -> 11L;       // 로맨스(10749)
            case 878 -> 12L;       // SF(878)
            case 10770 -> 13L;       // TV 영화(10770)
            case 27, 53 -> 14L;       // 공포(27), 스릴러(53)
            case 10752 -> 15L;       // 전쟁(10752)
            case 37 -> 16L;       // 서부(37)
            default -> 99999L;
        };
    }

    public void getMovieCrewEntityList() {
        List<MovieCrewEntity> movieCrewResultList = new ArrayList<>();
        List<MovieRCrewEntity> movieRCrewResultList = new ArrayList<>();

        // 언어, 지역
        String language = "&language=ko";
        List<MovieEntity> all = movieCollectRepository.findAll();

        all.forEach(movie -> {
            Long movieId = movie.getMovieId();
            String apiUrl =
                    "https://api.themoviedb.org/3/movie/" + movieId + "/credits" + "?api_key=" + key
                            + language;
            Map<String, Object> MovieCrewEntityInfo = restTemplate.getForObject(apiUrl, Map.class);

            // MovieCrewEntity
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
            List<Map<String, Object>> castList = (List<Map<String, Object>>) MovieCrewEntityInfo.get("cast");
            castList.forEach(cast -> {
                MovieCrewId movieCrewId = IdFactory.createMovieCrewId();
                String name = (String) cast.get("name");
                MovieRole role = MovieRole.CAST;
                String charName = (String) cast.get("character");
                String profileImgUrl = (String) cast.get("profile_path");
                int orderNo = (Integer) cast.get("order");

                MovieCrewEntity movieCrewEntity = MovieCrewEntity.builder()
                        .movieCrewId(movieCrewId)
                        .name(name)
                        .role(role)
                        .charName(charName)
                        .profileImgUrl(profileImgUrl)
                        .orderNo(orderNo)
                        .build();

                MovieRCrewIdForEntity movieRCrewIdForEntity = new MovieRCrewIdForEntity(movieId, movieCrewId);
                MovieEntity movieEntity = movieCollectRepository.findById(movieId).orElseThrow(
                        MovieNotFoundException::new);
                MovieRCrewEntity movieRCrewEntity = new MovieRCrewEntity(movieRCrewIdForEntity, movieCrewEntity,
                        movieEntity);

                movieCrewResultList.add(movieCrewEntity);
                movieRCrewResultList.add(movieRCrewEntity);
                log.info("name={}", name);
            });

            // 2. Crew (DIRECTOR) -> 0번째 배열, job = "Director"
            List<Map<String, Object>> crewList = (List<Map<String, Object>>) MovieCrewEntityInfo.get("crew");
            if (crewList.isEmpty()) {
                return;
            }
            Map<String, Object> crew = crewList.get(0);
            MovieCrewId movieCrewId = IdFactory.createMovieCrewId();
            String name = (String) crew.get("name");
            MovieRole role = MovieRole.DIRECTOR;
            String charName = (String) crew.get("character");
            String profileImgUrl = (String) crew.get("profile_path");
            int orderNo = -1;

            MovieCrewEntity movieCrewEntity = MovieCrewEntity.builder()
                    .movieCrewId(movieCrewId)
                    .name(name)
                    .role(role)
                    .charName(charName)
                    .profileImgUrl(profileImgUrl)
                    .orderNo(orderNo)
                    .build();

            MovieRCrewIdForEntity movieRCrewIdForEntity = new MovieRCrewIdForEntity(movieId, movieCrewId);
            MovieEntity movieEntity = movieCollectRepository.findById(movieId).orElseThrow(MovieNotFoundException::new);
            MovieRCrewEntity movieRCrewEntity = new MovieRCrewEntity(movieRCrewIdForEntity, movieCrewEntity,
                    movieEntity);

            movieCrewResultList.add(movieCrewEntity);
            movieRCrewResultList.add(movieRCrewEntity);
            log.info("name={}", name);
        });

        movieCrewJpaRepository.saveAll(movieCrewResultList);
        movieRCrewJpaRepository.saveAll(movieRCrewResultList);
    }

}
