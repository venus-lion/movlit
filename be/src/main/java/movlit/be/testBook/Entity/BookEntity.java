package movlit.be.testBook.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Builder
@Getter @Setter
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
public class BookEntity {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(255)")
    private String bookId; // isbn, uuid

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
    private String stockStatus = "in-stock"; // 필드 초기화 (재고있음)

    @Column
    private String mallUrl;

    @Column
    private Long heartCount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDt;

    @LastModifiedDate
    private LocalDateTime updDt;

}
