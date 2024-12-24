package movlit.be.testBook.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

// RoleType 파싱은 임의로 해서 안될 것임
@Entity
@Getter
@Setter
@Builder
@Table(name = "book_crew")
@NoArgsConstructor
@AllArgsConstructor
public class BookcrewEntity {
    public enum Role {
        AUTHOR, // 지은이
        TRANSLATOR, // 옮긴이
        UNKNOWN // 그 외
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "VARCHAR(255)")
    private String crewId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_image_url")
    private String profileImageUrl; // S3 기본 이미지 경로


}
