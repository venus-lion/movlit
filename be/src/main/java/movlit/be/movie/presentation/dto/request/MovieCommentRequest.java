package movlit.be.movie.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieCommentRequest {

    // TODO: score를 Double로 바꾸고, 0.5 간격으로 조정
    @NotNull
    private Integer score;

    @NotBlank
    @Size(min = 1, max = 1000, message = "코멘트 작성은 최소 1글자, 최대 1000글자입니다.")
    private String comment;

}
