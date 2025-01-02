package movlit.be.data_collection.movie.indexing;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collect/indices")
public class MovieIndexingController {

    @GetMapping("/movies")
    public void getMovieIndices(){

    }
}
