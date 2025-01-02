package movlit.be.bookES;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class BookESService {
    private final BookESRepository bookESRepository;

    // BestsellerBookES 저장 코드
    public void SaveBestsellerBookES(BookES bookES){
        bookESRepository.save(bookES);
    }

}
