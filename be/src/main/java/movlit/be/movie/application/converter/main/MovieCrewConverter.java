package movlit.be.movie.application.converter.main;

import movlit.be.movie.domain.MovieCrew;
import movlit.be.movie.domain.MovieRCrew;
import movlit.be.movie.domain.MovieRCrewId;
import movlit.be.movie.domain.entity.MovieCrewEntity;
import movlit.be.movie.domain.entity.MovieRCrewEntity;
import movlit.be.movie.domain.entity.MovieRCrewIdForEntity;

public class MovieCrewConverter {
    // RCrew Entity ->  RCrew Domain
    public static MovieRCrew toRCrewDomain(MovieRCrewEntity entity) {
        MovieRCrewIdForEntity entityId = entity.getMovieRCrewIdForEntity();
        MovieCrewEntity movieCrewEntity = entity.getMovieCrewEntity();

        return MovieRCrew.builder()
                .movieRCrewId(new MovieRCrewId(entityId.getMovieId(), entityId.getMovieCrewId()))
                .movieCrew(MovieCrewConverter.toCrewDomain(movieCrewEntity))
                .build();
    }


    // CrewEntity -> CrewDomain
    public static MovieCrew toCrewDomain(MovieCrewEntity entity) {
        return MovieCrew.builder()
                .movieCrewId(entity.getMovieCrewId())
                .name(entity.getName())
                .role(entity.getRole())
                .charName(entity.getCharName())
                .profileImgUrl(entity.getProfileImgUrl())
                .orderNo(entity.getOrderNo())
                .build();
    }
}