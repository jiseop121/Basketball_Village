package community.basketballvillage.dto.response;

import lombok.Getter;
import community.basketballvillage.domain.User;

@Getter
public class ResLoginDto {
    private final String name;
    private final String email;

    public ResLoginDto(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
