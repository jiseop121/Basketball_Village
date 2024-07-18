package community.basketballvillage.config;

import community.basketballvillage.dto.response.ResLoginDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResSecuSuccessDto {
    private final String message;
    private final int status;
    private final ResLoginDto data;
}
