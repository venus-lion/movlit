package movlit.be.book.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
book_genre 테이블 -> book_id, genre_id : 복합키

book_id	  genre_id          BookEntity
1	      101 (모험)          해리포터
1	      102 (판타지)         해리포터
2	      103 (로맨스)         타이타닉
3 	      104 (코미디)         슈렉
3	      103 (로맨스)         슈렉
이 코드를 통해 위와 같은 데이터가 저장됩니다.
BookEntity와 연관 매핑을 추가한 경우, JPA에서 객체 그래프 탐색을 통해 관련 데이터를 쉽게 조회할 수 있습니다

또, 하나의 책이 여러 개의 장르에 속할 수 있기에, BookEntity 중복 가능..
 */

@Entity
@Getter
@Setter
@Table(name = "book_genre")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class BookGenreEntity {

    @EmbeddedId
    private BookGenreIdEntity bookGenreIdEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId") // 복합 키의 bookId와 연결
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private BookEntity bookEntity;

}
