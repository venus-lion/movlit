package movlit.be.movie_comment_heart_count.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import movlit.be.common.util.IdFactory;
import movlit.be.common.util.ids.MovieCommentId;
import movlit.be.common.util.ids.MovieCommentLikeCountId;
import movlit.be.movie_comment_heart.presentation.dto.response.MovieCommentLikeResponse;
import movlit.be.movie_comment_heart_count.domain.entity.MovieCommentLikeCountEntity;
import movlit.be.movie_comment_heart_count.infra.persistence.jpa.MovieCommentLikeCountJpaRepository;
import movlit.be.utils.SpringBootIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MovieCommentLikeCountWriteServiceTest {

    @Autowired
    private MovieCommentLikeCountWriteService movieCommentLikeCountWriteService;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private MovieCommentLikeCountJpaRepository movieCommentLikeCountJpaRepository;

    @BeforeEach
    void setUp() {
        movieCommentLikeCountJpaRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        movieCommentLikeCountJpaRepository.deleteAll();
    }

    @DisplayName("코멘트 좋아요 카운트를 100번 비동기로 증가시키면 100번 증가한다.")
    @Test
    void increment() {
        // given
        MovieCommentId movieCommentId = new MovieCommentId("1");
        MovieCommentLikeCountId movieCommentLikeCountId = IdFactory.createMovieCommentLikeCountId();
        movieCommentLikeCountJpaRepository.save(
                new MovieCommentLikeCountEntity(movieCommentLikeCountId, movieCommentId, 1L));
        CountDownLatch latch = new CountDownLatch(100);

        // when
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(() -> {
                movieCommentLikeCountWriteService.incrementMovieCommentLikeCount(movieCommentId);
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // then
        var response = movieCommentLikeCountJpaRepository.findByMovieCommentId(movieCommentId);
        assertThat(response).isPresent();
        assertThat(response.get()).hasFieldOrPropertyWithValue("count", 101L);
    }


}