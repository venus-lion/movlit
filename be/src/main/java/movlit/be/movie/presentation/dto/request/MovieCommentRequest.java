package movlit.be.movie.presentation.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
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

    @NotNull
    @DecimalMin(value = "0.5", message = "별점은 0.5 이상이어야 합니다.")
    @DecimalMin(value = "10.0", message = "별점은 10.0 이하여야 합니다.")
    @Digits(integer = 1, fraction = 1, message = "별점은 소숫점 첫째 자리까지만 가능합니다.")
    private Double score;

    @NotBlank
    @Size(min = 1, max = 1000, message = "코멘트 작성은 최소 1글자, 최대 1000글자입니다.")
    private String comment;

}
