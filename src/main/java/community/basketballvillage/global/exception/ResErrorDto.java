package community.basketballvillage.global.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResErrorDto {
    private String message;
    private int status;
    private String code;

    public ResErrorDto(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getHttpStatus().value();
        this.code = code.getCode();
    }
}
