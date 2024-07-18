package community.basketballvillage.global.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ApiResponse(
        responseCode = "400",
        description = "Validation에서 발생되는 DTO에러",
        content = @Content(schema = @Schema(implementation = ResValidErrorDto.class))
    )
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ResValidErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);

        final ResValidErrorDto response = ResValidErrorDto.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * enum type 일치하지 않아 binding 못할 경우 발생
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     */
    @ApiResponse(
        responseCode = "400",
        description = "파라미터 데이터 형식 오류 발생",
        content = @Content(schema = @Schema(implementation = ResValidErrorDto.class))
    )
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ResValidErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        final ResValidErrorDto response = ResValidErrorDto.of(e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ResValidErrorDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        final ResValidErrorDto response = ResValidErrorDto.of(ErrorCode.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생합
     */
    @ApiResponse(
        responseCode = "403",description = "권한 없음"
    )
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ResValidErrorDto> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException", e);
        final ResValidErrorDto response = ResValidErrorDto.of(ErrorCode.HANDLE_ACCESS_DENIED);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ErrorCode.HANDLE_ACCESS_DENIED.getHttpStatus().value()));
    }

    @ApiResponse(
        responseCode = "4XX",description = "서버 정상적 에러 처리"
    )
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResErrorDto> handleBusinessException(final BusinessException e) {
        log.error("handleBusinessException", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ResErrorDto response = new ResErrorDto(e.getErrorCode());
        return new ResponseEntity<>(response, HttpStatus.valueOf(errorCode.getHttpStatus().value()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ResErrorDto> handleNoResourceFoundException(NoResourceFoundException e){
        log.error("handleNoResourceFoundException", e);
        final ResValidErrorDto response = ResValidErrorDto.of(ErrorCode.URL_NOT_FOUND);
        ResErrorDto resErrorDto = new ResErrorDto(ErrorCode.URL_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resErrorDto);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ResValidErrorDto> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        final ResValidErrorDto response = ResValidErrorDto.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
