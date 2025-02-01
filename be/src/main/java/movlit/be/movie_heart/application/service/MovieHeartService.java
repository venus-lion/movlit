package movlit.be.movie_heart.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.common.exception.MovieHeartAlreadyExistsException;
import movlit.be.common.util.ids.BookId;
import movlit.be.common.util.ids.MemberId;
import movlit.be.member.application.service.MemberReadService;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie_heart.domain.MovieHeart;
import movlit.be.movie_heart.domain.entity.MovieHeartEntity;
import movlit.be.movie_heart.domain.repository.MovieHeartRepository;
import movlit.be.movie_heart.presentation.dto.response.MovieHeartResponse;
import movlit.be.movie_heart_count.application.service.MovieHeartCountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieHeartService {

    private final MovieHeartRepository movieHeartRepository;
    private final MovieHeartCountService movieHeartCountService;
    private final MemberReadService memberReadService;

    @Transactional
    public MovieHeartResponse heart(Long movieId, MemberId memberId) {
        memberReadService.validateMemberIdExists(memberId);
        validateHeartExists(movieId, memberId);
        MovieHeartEntity movieHeartEntity = movieHeartRepository.heart(MovieConvertor.toMovieHeartEntity(movieId, memberId));
        movieHeartCountService.incrementMovieHeartCount(movieHeartEntity.getMovieId());
        Long movieHeartCount = movieHeartCountService.fetchMovieHeartCountByMovieId(movieHeartEntity.getMovieId());
        return MovieConvertor.toMovieHeartResponse(movieHeartEntity, movieHeartCount);
    }

    @Transactional
    public void unHeart(Long movieId, MemberId memberId) {
        memberReadService.validateMemberIdExists(memberId);
        validateHeartNotExist(movieId, memberId);
        movieHeartRepository.deleteByMovieIdAndMemberId(movieId, memberId);
        movieHeartCountService.decrementMovieHeartCount(movieId);
    }

    @Transactional(readOnly = true)
    public void validateHeartExists(Long movieId, MemberId memberId) {
        if (movieHeartRepository.existsByMovieIdAndMemberId(movieId, memberId)) {
            throw new MovieHeartAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public void validateHeartNotExist(Long movieId, MemberId memberId) {
        if (!movieHeartRepository.existsByMovieIdAndMemberId(movieId, memberId)) {
            throw new MovieHeartAlreadyExistsException();
        }
    }

    @Transactional(readOnly = true)
    public List<MovieHeart> fetchMovieHeartRecentByMember(MemberId memberId) {
        return movieHeartRepository.fetchMovieHeartRecentByMember(memberId);
    }

    // 해당 영화를 찜한 멤버Id 리스트 가져오기
    public List<MemberId> fetchHeartingMemberIdsByMovieId(Long movieId){
        List<MemberId> heartingMemberIds = movieHeartRepository.fetchHeartingMembersByMovieId(movieId);

        return heartingMemberIds;
    }

}
