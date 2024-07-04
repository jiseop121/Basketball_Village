package community.basketballvillage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import community.basketballvillage.dto.request.JoinDto;
import community.basketballvillage.global.constant.Role;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResUserInfoDto {

    private String name;
    private String email;
    private Role role;
    private String password;

    public ResUserInfoDto(JoinDto joinDto) {
        this.name = joinDto.getName();
        this.email = joinDto.getEmail();
        this.role = joinDto.getRole();
        this.password = joinDto.getPassword();
    }
}
