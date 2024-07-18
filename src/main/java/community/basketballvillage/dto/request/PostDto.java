package community.basketballvillage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    @NotEmpty(message = "제목을 입력해주세요.")
    @NotBlank(message = "제목은 공백일 수 없습니다.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    @NotBlank(message = "내용은 공백일 수 없습니다.")
    private String content;
}
