package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookHeartCount {

    private Long bookHeartCountId;
    private BookHeart bookHeart;
    private Long count; // 해당 책의 "찜"(heart) 갯수
    private Long version;


}
