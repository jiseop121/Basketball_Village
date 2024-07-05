package community.basketballvillage.config;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import community.basketballvillage.dto.response.ResLoginDto;
import community.basketballvillage.global.exception.ErrorCode;
import community.basketballvillage.global.exception.ResErrorDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;

@Slf4j
public class CustomResponseUtil {

    public static void success(HttpServletResponse response, ResLoginDto dto) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResSecuSuccessDto resSecuSuccessDto = new ResSecuSuccessDto("로그인을 정상적으로 성공하였습니다.",200, dto);
            String responseBody = om.writeValueAsString(resSecuSuccessDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(200);
            response.getWriter().println(responseBody);
        } catch (Exception e) { //사실 파싱 에러가 날 수 없는 구조다.
            log.error("서버 파싱 에러");
        }
    }

    public static void fail(HttpServletResponse response, Exception exception, HttpStatus httpStatus) {
        log.info("Spring Security fail : "+exception.getClass().getName());
        try {
            ObjectMapper om = new ObjectMapper();

            ErrorCode errorCode = getErrorCode(exception);
            String responseBody = om.writeValueAsString(
                new ResErrorDto(errorCode)
            );
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody);
        } catch (Exception e) { //사실 파싱 에러가 날 수 없는 구조다.
            log.error("server pasring error");
        }
    }

    private static ErrorCode getErrorCode(Exception exception) {
        //로그인 예외 처리
        if(exception instanceof InternalAuthenticationServiceException){
            return ErrorCode.USER_NOT_FOUND;
        }

        //auth 관련 예외 처리
        else if(exception instanceof AuthenticationException){ //InsufficientAuthenticationException
            return ErrorCode.INSUFFICIENT_AUTH;
        }
        else if(exception instanceof AccessDeniedException){
            return ErrorCode.INSUFFICIENT_PERMISSIONS;
        }

        //JWT TOKEN 관련 예외 처리
        else if (exception instanceof JWTCreationException) {
            return ErrorCode.JWT_CREATION_ERROR;
        }
        else if (exception instanceof JWTVerificationException) {
            if (exception instanceof TokenExpiredException) {
                return ErrorCode.TOKEN_EXPIRED_ERROR;
            } else if (exception instanceof SignatureVerificationException) {
                return ErrorCode.JWT_SIGNATURE_ERROR;
            } else if (exception instanceof AlgorithmMismatchException) {
                return ErrorCode.JWT_ALGORITHM_MISMATCH_ERROR;
            } else if (exception instanceof JWTDecodeException) {
                return ErrorCode.JWT_DECODE_ERROR;
            } else {
                return ErrorCode.JWT_VERIFICATION_ERROR;
            }
        } else {
            return ErrorCode.JWT_VERIFICATION_ERROR; // 기본 오류 코드
        }
    }
}
