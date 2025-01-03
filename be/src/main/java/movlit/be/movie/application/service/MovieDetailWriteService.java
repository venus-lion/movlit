package movlit.be.movie.application.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.application.converter.detail.MovieConvertor;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.presentation.dto.request.MovieCommentData;
import movlit.be.movie.presentation.dto.response.MovieCommentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MovieDetailWriteService {

    private final MovieCommentRepository movieCommentRepository;

    public MovieCommentResponse createComment(MovieCommentData data) {
        MovieCommentId movieCommentId = IdFactory.createMovieCommentId();
        LocalDateTime now = LocalDateTime.now();
        MovieCommentEntity movieCommentEntity = MovieConvertor.toMovieCommentEntity(data, movieCommentId, now);
        return MovieConvertor.toMovieCommentResponse(movieCommentRepository.createComment(movieCommentEntity));
    }

}
