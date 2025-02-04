package movlit.be.data_collection.movie.indexing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.data_collection.movie.jpa.MovieCollectRepository;
import movlit.be.movie.application.converter.main.MovieDocumentConverter;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.entity.MovieEntity;
import movlit.be.movie.infra.persistence.es.MovieDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MovieIndexService {

    private final MovieCollectRepository movieCollectRepository;
    private final MovieDocumentRepository movieDocumentRepository;

    public List<MovieDocument> getMovieIndices() {
        List<MovieEntity> movieList = movieCollectRepository.findAll();
        List<MovieDocument> movieDocumentList = new ArrayList<>();
        AtomicInteger cnt = new AtomicInteger(1);

        // Movie정보(MovieEntity, MovieGenreEntity, MovieTagEntity)를 Document로 convert
        movieList.forEach(movie -> {
            MovieDocument movieDocument = MovieDocumentConverter.entityToDocument(movie);
            movieDocumentList.add(movieDocument);

            if (cnt.get() == 40) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            movieDocumentRepository.saveAll(movieDocumentList);
            movieDocumentList.clear();
            cnt.getAndIncrement();
        });

        return movieDocumentList;
    }

}
