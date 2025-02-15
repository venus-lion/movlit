package movlit.be.book.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
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
import movlit.be.common.util.ids.BookcrewId;
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

    @EmbeddedId
    private BookcrewId crewId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_image_url")
    private String profileImageUrl; // S3 기본 이미지 경로


}
