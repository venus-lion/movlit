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
public class BookHeartCountVo {

    private Long bookHeartCountId;
    private BookVo bookVo;
    private int count; // 해당 책의 "찜"(heart) 갯수
    private Long version;


}
