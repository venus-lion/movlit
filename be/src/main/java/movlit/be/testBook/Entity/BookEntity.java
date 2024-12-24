package movlit.be.testBook.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter @Setter
@Table(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BookEntity {

    @Id
//    @GeneratedValue(generator = "uuid2")
//    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(255)")
    private String bookId; // isbn13 , uuid

    @Column
    private String isbn; // isbn

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String publisher;

    @Column(name = "pub_date")
    private LocalDateTime pubDate;

    @Column
    private String description;

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

}
