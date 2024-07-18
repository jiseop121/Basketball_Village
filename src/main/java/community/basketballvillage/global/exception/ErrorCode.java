package community.basketballvillage.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

@Getter
@JsonFormat(shape = Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 메서드입니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "C003", "존재하지 않는 엔티티입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 오류입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "잘못된 타입 값입니다."),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C006", "접근이 거부되었습니다."),
    URL_NOT_FOUND(HttpStatus.NOT_FOUND, "C007", "존재하지 않는 주소입니다."),

    // User
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "U001", "이메일이 중복되었습니다."),
    LOGIN_INPUT_INVALID(HttpStatus.BAD_REQUEST, "U002", "로그인 입력이 잘못되었습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U003", "존재하지 않는 유저입니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "P002", "게시물을 찾을 수 없습니다."),
    POST_USER_MISMATCH(HttpStatus.BAD_REQUEST, "P003", "게시물이 사용자와 일치하지 않습니다."),

    // JWT
    JWT_DECODE_ERROR(HttpStatus.BAD_REQUEST, "J001", "JWT 토큰 형식이 잘못되었습니다."),
    JWT_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "J002", "JWT 토큰 생성 중 오류가 발생했습니다."),
    JWT_VERIFICATION_ERROR(HttpStatus.UNAUTHORIZED, "J003", "JWT 토큰 검증 중 오류가 발생했습니다."),
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED, "J004", "JWT 토큰이 만료되었습니다."),
    JWT_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED, "J005", "JWT 토큰 서명이 유효하지 않습니다."),
    JWT_ALGORITHM_MISMATCH_ERROR(HttpStatus.BAD_REQUEST, "J006", "JWT 알고리즘이 일치하지 않습니다."),
    JWT_INVALID_CLAIM_ERROR(HttpStatus.BAD_REQUEST, "J007", "JWT 클레임이 유효하지 않습니다."),
    LOGIN_USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "J003", "아이디 또는 비밀번호가 잘못 되었습니다. 아이디와 비밀번호를 정확히 입력해 주세요."),


    // Auth
    INSUFFICIENT_AUTH(HttpStatus.UNAUTHORIZED,"A001","인증이 충분하지 않습니다."),
    INSUFFICIENT_PERMISSIONS(HttpStatus.FORBIDDEN, "A001", "접근 가능한 권한이 없습니다.");

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.code = code;
    }
}
