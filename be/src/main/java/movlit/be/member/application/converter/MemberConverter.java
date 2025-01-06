package movlit.be.member.application.converter;

import java.time.LocalDateTime;
import java.util.List;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MemberId;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.member.domain.Member;
import movlit.be.member.domain.entity.MemberEntity;
import movlit.be.member.domain.entity.MemberGenreEntity;
import movlit.be.member.domain.entity.MemberGenreIdEntity;
import movlit.be.member.presentation.dto.request.MemberRegisterOAuth2Request;
import movlit.be.member.presentation.dto.request.MemberRegisterRequest;
import movlit.be.member.presentation.dto.request.MemberUpdateRequest;
import movlit.be.member.presentation.dto.response.MemberRegisterResponse;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.presentation.dto.request.MovieCommentData;
import movlit.be.movie.presentation.dto.request.MovieCommentDataForDelete;
import movlit.be.movie.presentation.dto.request.MovieCommentRequest;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import movlit.be.movie_comment_heart.domain.entity.MovieCommentLikeEntity;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;
import movlit.be.movie_heart.presentation.dto.response.MovieHeartResponse;
import movlit.be.movie_heart_count.domain.entity.MovieHeartCountEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class MemberConverter {

    private MemberConverter() {
        // TODO : 공통적인 예외처리 등록해주기
    }

    // Domain -> Entity
    public static MemberEntity toEntity(Member member) {
        return MemberEntity.builder()
                .memberId(member.getMemberId())
                .memberGenreEntityList(member.getMemberGenreEntityList())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .dob(member.getDob())
                .profileImgId(member.getProfileImgId())
                .profileImgUrl(member.getProfileImgUrl())
                .role(member.getRole())
                .provider(member.getProvider())
                .regDt(member.getRegDt())
                .updDt(member.getUpdDt())
                .build();
    }

    // Entity -> Domain
    public static Member toDomain(MemberEntity memberEntity) {
        return Member.builder()
                .memberId(memberEntity.getMemberId())
                .email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname())
                .password(memberEntity.getPassword())
                .dob(memberEntity.getDob())
                .profileImgId(memberEntity.getProfileImgId())
                .profileImgUrl(memberEntity.getProfileImgUrl())
                .role(memberEntity.getRole())
                .provider(memberEntity.getProvider())
                .regDt(memberEntity.getRegDt())
                .updDt(memberEntity.getUpdDt())
                .build();
    }

    // Domain to Dto
    public static MemberRegisterResponse toRegisterResponse(Member member) {
        return MemberRegisterResponse.from(member.getMemberId());
    }

    public static MemberGenreEntity toMemberGenreEntity(Long genreId, MemberId memberId) {
        return new MemberGenreEntity(new MemberGenreIdEntity(genreId, memberId));
    }

    public static MemberEntity toMemberEntity(MemberRegisterRequest request,
                                              List<MemberGenreEntity> memberGenreEntityList,
                                              MemberId memberId) {
        return MemberEntity.builder()
                .memberId(memberId)
                .password(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()))
                .memberGenreEntityList(memberGenreEntityList)
                .nickname(request.getNickname())
                .email(request.getEmail())
                .dob(request.getDob())
                .regDt(LocalDateTime.now())
                .role("ROLE_Member")
                .provider("local")
                .build();
    }

    public static MemberRegisterResponse toMemberRegisterResponse(MemberId memberId) {
        return MemberRegisterResponse.from(memberId);
    }

    public static Member toMemberForUpdate(MemberUpdateRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(request.getPassword())
                .dob(request.getDob())
//                .profileImgId() TODO
//                .profileImgUrl()
                .updDt(LocalDateTime.now())
                .build();
    }


    public static MovieCommentData toMovieDetailCommentData(Long movieId, MemberId memberId,
                                                            MovieCommentRequest request) {
        return new MovieCommentData(movieId, memberId, request);
    }

    public static MovieCommentDataForDelete toMovieDetailCommentDataForDelete(MemberId memberId,
                                                                              MovieCommentId movieCommentId) {
        return new MovieCommentDataForDelete(memberId, movieCommentId);
    }

    public static MovieCommentEntity toMovieCommentEntity(MovieCommentData data, MovieCommentId movieCommentId,
                                                          LocalDateTime now) {
        return MovieCommentEntity.builder()
                .movieCommentId(movieCommentId)
                .memberId(data.memberId())
                .movieId(data.movieId())
                .score(data.request().getScore())
                .comment(data.request().getComment())
                .build();
    }

    public static MovieCommentResponse toMovieCommentResponse(MovieCommentId comment) {
        return new MovieCommentResponse(comment);
    }

    public static MovieHeartResponse toMovieHeartResponse(MovieHeartEntity movieHeartEntity, Long movieHeartCount) {
        return MovieHeartResponse.builder()
                .movieHeartId(movieHeartEntity.getMovieHeartId())
                .isHearted(movieHeartEntity.isHearted())
                .movieId(movieHeartEntity.getMovieId())
                .memberId(movieHeartEntity.getMemberId())
                .movieHeartCnt(movieHeartCount)
                .build();
    }

    public static MovieCommentLikeResponse toMovieCommentLikeResponse(MovieCommentLikeEntity movieCommentLikeEntity,
                                                                      Long movieCommentLikeCount) {
        return MovieCommentLikeResponse.builder()
                .movieCommentLikeId(movieCommentLikeEntity.getMovieCommentLikeId())
                .movieCommentId(movieCommentLikeEntity.getMovieCommentId())
                .memberId(movieCommentLikeEntity.getMemberId())
                .isLiked(movieCommentLikeEntity.isLiked())
                .movieCommentLikeCount(movieCommentLikeCount)
                .build();
    }

    public static MovieHeartEntity toMovieHeartEntity(Long movieId, MemberId memberId) {
        return MovieHeartEntity.builder()
                .movieHeartId(IdFactory.createMovieHeartId())
                .movieId(movieId)
                .memberId(memberId)
                .isHearted(true)
                .build();
    }

    public static MovieCommentLikeEntity toMovieCommentLikeEntity(MemberId memberId,
                                                                  MovieCommentId movieCommentId) {
        return MovieCommentLikeEntity.builder()
                .movieCommentLikeId(IdFactory.createMovieCommentLikeId())
                .movieCommentId(movieCommentId)
                .memberId(memberId)
                .isLiked(true)
                .build();
    }

    public static MovieHeartCountEntity toMovieHeartCountEntity(Long movieId) {
        return MovieHeartCountEntity.builder()
                .movieHeartCountId(IdFactory.createMovieHeartCountId())
                .movieId(movieId)
                .count(0L)
                .build();
    }

    public static MovieCommentLikeCountEntity toMovieCommentLikeCountEntity(MovieCommentEntity movieCommentEntity) {
        return MovieCommentLikeCountEntity.builder()
                .movieCommentLikeCountId(IdFactory.createMovieCommentLikeCountId())
                .movieCommentId(movieCommentEntity.getMovieCommentId())
                .count(0L)
                .build();
    }

}
