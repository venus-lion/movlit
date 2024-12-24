package movlit.be.data_collection;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MovieCollectServiceTest {

    @DisplayName("날짜 계산 가능한지 테스트")
    @Test
    void calculateDateTest() {
        LocalDate today = LocalDate.now();
        System.out.println("today = " + today);

        LocalDate minus = today.minusWeeks(1);
        System.out.println("minus = " + minus);

        LocalDate plusDays = minus.plusDays(1);
        System.out.println("plusDays = " + plusDays);
    }

}