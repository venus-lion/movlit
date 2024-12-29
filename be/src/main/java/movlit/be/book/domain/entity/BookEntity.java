package movlit.be.book.domain.entity;

import com.mysql.cj.protocol.ColumnDefinition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import movlit.be.common.util.ids.BookId;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter
@Setter
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BookEntity {

    @EmbeddedId
    private BookId bookId; // isbn13 , uuid

    @Column
    private String isbn; // isbn

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String publisher;

    @Column(name = "pub_date")
    private LocalDateTime pubDate;

    @Lob
    private String description;

    @Column
    private String categoryName;

    @Column(nullable = false)
    private String bookImgUrl;

    @Column(nullable = false, name = "stock_status")
    private String stockStatus;

    @Column
    private String mallUrl;

    @Column
    private Long heartCount;

    @CreatedDate
    private LocalDateTime regDt;

    @LastModifiedDate
    private LocalDateTime updDt;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookRCrewEntity> bookRCrewEntities = new ArrayList<>();
}
