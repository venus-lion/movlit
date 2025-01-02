package movlit.be.data_collection.movie.indexing;

import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.movie.domain.document.MovieDocument;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect/indices")
@RequiredArgsConstructor
public class MovieIndexingController {

    private final MovieIndexService movieIndexService;

    @GetMapping("/movies")
    public void getMovieIndices(){
        List<MovieDocument> movieDocumentList = movieIndexService.getMovieIndices();


    }
}
