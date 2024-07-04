package community.basketballvillage.dto.response;

import lombok.Getter;
import community.basketballvillage.domain.User;
import community.basketballvillage.util.CustomDateUtil;

@Getter
public class ResLoginDto {
    private final Long id;
    private final String email;
    private final String createdAt;

    public ResLoginDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
    }
}
