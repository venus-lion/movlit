package movlit.be.data_collection.movie.indexing;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import movlit.be.data_collection.movie.jpa.MovieCollectRepository;
import movlit.be.movie.domain.document.MovieDocument;
import movlit.be.movie.domain.entity.MovieEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieIndexService {

    private final MovieCollectRepository movieCollectRepository;

    public List<MovieDocument> getMovieIndices(){
        List<MovieEntity> movieList = movieCollectRepository.findAll();

        log.info("=== movieList.size() = {}", movieList.size());

        return null;
    }
}
