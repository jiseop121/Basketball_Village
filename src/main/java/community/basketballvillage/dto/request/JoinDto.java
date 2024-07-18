package community.basketballvillage.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import community.basketballvillage.global.constant.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinDto {

    @NotEmpty(message = "이름을 입력해주세요.")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    @Email(message = "유효한 이메일 형식을 입력해주세요.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Size(min = 8, max = 20, message = "비밀번호의 길이는 최소 8자에서 최대 20자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).*$", message = "비밀번호에는 최소 하나의 숫자, 하나의 문자, 하나의 특수 문자가 포함되어야 합니다.")
    private String password;

    @NotNull(message = "잘못된 입력값입니다.")
    private Role role;

    // Getters and setters
}