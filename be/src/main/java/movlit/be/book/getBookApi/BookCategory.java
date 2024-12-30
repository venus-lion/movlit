package movlit.be.book.getBookApi;

import static movlit.be.book.getBookApi.GetBookBestService.*;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import movlit.be.book.domain.Genre;
import movlit.be.book.domain.entity.BookEntity;
import movlit.be.book.domain.entity.BookGenreEntity;
import movlit.be.book.infra.persistence.jpa.BookGenreJpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookCategory {
    private final GetBookBestService getBookBestService;
    public void classifyAndSaveBooks(String bookCode) {
        // Save the category if applicable
        if (isActionCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.ACTION);
        }
        if (isAnimationCategory(bookCode)) {
           // GetBookBestService.saveBookToDatabase(bookCode, Genre.ANIMATION);
            getBookBestService.saveBookToDatabase(bookCode, Genre.ANIMATION);
        }
        if (isComedyCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.COMEDY);
        }
        if (isCrimeCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.CRIME);
        }
        //ㅁ완
        if (isCrimeCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.CRIME);
        }
        if (isDocumentaryCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.DOCUMENTARY);
        }
        if (isDramaCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.DRAMA);
        }
        if (isFantasyCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.FANTASY);
        }
        if (isHistoryCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.HISTORY);
        }
        if (isMusicCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.MUSIC);
        }
        if (isMysteryCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.MYSTERY);
        }
        if (isRomanceCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.ROMANCE);
        }
        if (isSFCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.SCIENCE_FICTION);
        }
        if (isTV_MOVIECategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.TV_MOVIE);
        }
        if (isTHRILLERCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.THRILLER);
        }
        if (isWARCategory(bookCode)) {
            getBookBestService.saveBookToDatabase(bookCode, Genre.WAR);
        }
        if(isUNKNOWNCategory(bookCode)){
            getBookBestService.saveBookToDatabase(bookCode, Genre.UNKNOWN);
        }
    }


    //  ACTION(1, "액션"), // + 모험
    private boolean isActionCategory(String bookCode) {
        return
        "50933".equals(bookCode) ||
                "3724".equals(bookCode) ||
                "4133".equals(bookCode);
    }

    // ANIMATION(2, "애니메이션")
    private boolean isAnimationCategory(String bookCode) {
        return "4298".equals(bookCode) ||
                 "4302".equals(bookCode) ||
                 "4301".equals(bookCode) ||
                 "4303".equals(bookCode) ||
                 "2924".equals(bookCode);

    }


    // COMEDY(3, "코미디")
    private boolean isComedyCategory(String bookCode) {
        return "2552".equals(bookCode) ||
                "4302".equals(bookCode);
    }

    // CRIME(4, "범죄")
    private boolean isCrimeCategory(String bookCode) {
        return "2556".equals(bookCode) ||
                 "50926".equals(bookCode) ||
                 "51067".equals(bookCode) ||
                 "51062".equals(bookCode) ||
                 "51058".equals(bookCode) ||
                 "51065".equals(bookCode);
    }

    // DOCUMENTARY(5, "다큐멘터리")
    private boolean isDocumentaryCategory(String bookCode) {
        return "48899".equals(bookCode) ||
                 "48901".equals(bookCode) ||
                 "48902".equals(bookCode) ||
                 "51377".equals(bookCode) ||
                 "51423".equals(bookCode) ||
                 "51425".equals(bookCode) ||
                 "51842".equals(bookCode) ||
                 "51843".equals(bookCode) ||
                 "51844".equals(bookCode) ||
                 "51845".equals(bookCode) ||
                 "51373".equals(bookCode) ||
                 "51394".equals(bookCode) ||
                 "50827".equals(bookCode) ||
                 "51381".equals(bookCode) ||
                 "51416".equals(bookCode) ||
                 "52906".equals(bookCode) ||
                 "52904".equals(bookCode);
    }

    //  DRAMA(6, "드라마")
    private boolean isDramaCategory(String bookCode) {
        return "51242".equals(bookCode) ||
                 "50919".equals(bookCode) ||
                 "50918".equals(bookCode) ||
                 "50996".equals(bookCode) ||
                 "50998".equals(bookCode) ||
                 "51242".equals(bookCode) ||
                 "51239".equals(bookCode) ||
                 "50917".equals(bookCode) ||
                 "50994".equals(bookCode) ||
                 "50993".equals(bookCode);
    }

    // FANTASY(7, "판타지")
    private boolean isFantasyCategory(String bookCode) {
        return "50928".equals(bookCode) ||
                 "51120".equals(bookCode) ||
                 "51122".equals(bookCode) ||
                 "4133".equals(bookCode) ||
                 "4134".equals(bookCode) ||
                 "4135".equals(bookCode) ||
                 "4132".equals(bookCode);
    }

    // HISTORY(8, "역사")
    private boolean isHistoryCategory(String bookCode) {
        return "4670".equals(bookCode) ||
                 "50929".equals(bookCode) ||
                 "49220".equals(bookCode) ||
                 "48813".equals(bookCode) ||
                 "50883".equals(bookCode) ||
                 "74".equals(bookCode);
    }

    // MUSIC(9, "음악")
    private boolean isMusicCategory(String bookCode) {
        return "50966".equals(bookCode) ||
                 "51012".equals(bookCode) ||
                 "51214".equals(bookCode) ||
                 "51000".equals(bookCode);
    }

    // MYSTERY(10, "미스터리")
    private boolean isMysteryCategory(String bookCode) {
        return "2556".equals(bookCode) ||
                "50926".equals(bookCode) ||
                "51067".equals(bookCode) ||
                "51062".equals(bookCode) ||
                "51058".equals(bookCode) ||
                "51065".equals(bookCode);
    }

    // ROMANCE(11, "로맨스")
    private boolean isRomanceCategory(String bookCode) {
        return "51107".equals(bookCode) ||
                 "50935".equals(bookCode) ||
                 "51126".equals(bookCode) ||
                 "51107".equals(bookCode) ||
                 "50935".equals(bookCode) ||
                 "51126".equals(bookCode) ||
                 "51125".equals(bookCode);
    }

    // SCIENCE_FICTION(12, "SF")
    private boolean isSFCategory(String bookCode) {
        return "2553".equals(bookCode) ||
               "50930".equals(bookCode);
    }

    // TV_MOVIE(13, "TV 영화")
    private boolean isTV_MOVIECategory(String bookCode) {
        return "48949".equals(bookCode) ||
                "50967".equals(bookCode) ||
                "4301".equals(bookCode) ||
                "51051".equals(bookCode); // TV 영화 완료
    }

    // THRILLER(14, "공포, 스릴러")
    private boolean isTHRILLERCategory(String bookCode) {
        return "3723".equals(bookCode) ||
                "50933".equals(bookCode) ||
                "50931".equals(bookCode) ; // 스릴러 완료
    }

    // WAR(15, "전쟁")
    private boolean isWARCategory(String bookCode) {
        return "51407".equals(bookCode) ||
                "51253".equals(bookCode) ||
                "48908".equals(bookCode) ||
                "52610".equals(bookCode) ||
                "52617".equals(bookCode) ||
                "3826".equals(bookCode) ||
                "1961".equals(bookCode) ||
                "84".equals(bookCode) ||
                "52591".equals(bookCode) ||
                "1753".equals(bookCode); // 전쟁 완료
    }

    //  UNKNOWN(20, "기타")
    private boolean isUNKNOWNCategory(String bookCode) {
        return !isActionCategory(bookCode) ||
                !isAnimationCategory(bookCode) ||
                !isComedyCategory(bookCode) ||
                !isCrimeCategory(bookCode) ||
                !isDocumentaryCategory(bookCode) ||
                !isDramaCategory(bookCode) ||
                !isFantasyCategory(bookCode) ||
                !isHistoryCategory(bookCode) ||
                !isMusicCategory(bookCode) ||
                !isMysteryCategory(bookCode) ||
                !isRomanceCategory(bookCode) ||
                !isSFCategory(bookCode) ||
                !isTV_MOVIECategory(bookCode) ||
                !isTHRILLERCategory(bookCode) ||
                !isWARCategory(bookCode);
    }
}