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
public class BookHeartVo {

    private Long bookHeartId;
    private BookVo bookVo;
    private Member member;
    Boolean isHearted;

}
