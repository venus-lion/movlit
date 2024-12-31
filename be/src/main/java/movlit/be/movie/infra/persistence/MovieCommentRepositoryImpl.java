package movlit.be.movie.infra.persistence;

import lombok.RequiredArgsConstructor;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.movie.domain.entity.MovieCommentEntity;
import movlit.be.movie.domain.repository.MovieCommentRepository;
import movlit.be.movie.infra.persistence.jpa.MovieCommentJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MovieCommentRepositoryImpl implements MovieCommentRepository {

    private final MovieCommentJpaRepository movieCommentJpaRepository;

    @Override
    public MovieCommentId createComment(MovieCommentEntity movieCommentEntity) {
        MovieCommentEntity savedMovieCommentEntity = movieCommentJpaRepository.save(movieCommentEntity);
        return savedMovieCommentEntity.getMovieCommentId();
    }

}
