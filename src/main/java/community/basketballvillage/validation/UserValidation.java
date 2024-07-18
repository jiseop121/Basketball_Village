package community.basketballvillage.validation;

import org.springframework.stereotype.Component;
import community.basketballvillage.global.exception.BusinessException;
import community.basketballvillage.global.exception.ErrorCode;

@Component
public class UserValidation {

    public void checkEmailIsDupl(boolean isExist) {
        if(isExist){
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
        }
    }
}
