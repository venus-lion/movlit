package movlit.be.book.domain;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import movlit.be.book.domain.entity.BookEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BookHeartCount {

    private Long bookHeartCountId;
    private Book book;
    private int count; // 해당 책의 "찜"(heart) 갯수
    private Long version;


}
