package community.basketballvillage.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import community.basketballvillage.global.constant.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserDto {
    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private Role role;
}
