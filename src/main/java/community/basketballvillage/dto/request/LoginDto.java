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
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @NotBlank
    private String password;
}
