package community.basketballvillage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    private String password;
}
