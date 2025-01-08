package movlit.be.book.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.member.domain.Member;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookHeart {

    private Long bookHeartId;
    private Book book;
    private Member member;
    Boolean isHearted;

}
