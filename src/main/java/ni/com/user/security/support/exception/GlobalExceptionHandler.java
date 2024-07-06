package ni.com.user.security.support.exception;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.ApiResponseDto;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageResource messageResource;

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        List<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });
        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(messageResource.getMessage("error.argumentNotValid"))
                                .response(errorMessage)
                                .build()
                );
    }

    @ExceptionHandler(value = {ValueAlreadyExistsException.class
            , IllegalArgumentException.class
            , AuthenticationException.class
            , JwtException.class})
    public ResponseEntity<ApiResponseDto<?>> userAlreadyExistsExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {RoleNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ApiResponseDto<?>> roleNotFoundExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(value = {SQLException.class})
    public ResponseEntity<ApiResponseDto<?>> sqlExceptionHandler(SQLException exception) {
        return ResponseEntity
                .badRequest()
                .body(
                        ApiResponseDto.builder()
                                .isSuccess(false)
                                .message(sqlMessage(exception))
                                .build()
                );
    }

    private String sqlMessage(SQLException ex) {
        if (ex.getErrorCode() == 23505 && ex.getMessage().contains("UK_POST_EMAIL")) {
            return messageResource.getMessage("error.sqlUniqueEmail");
        } else if (ex.getErrorCode() == 23505 && ex.getMessage().contains("UK_POST_USER")) {
            return messageResource.getMessage("error.sqlUniqueUser");
        } else {
            return messageResource.getMessage("error.sqlException", new Object[]{ex.getErrorCode(), ex.getMessage()});
        }
    }

}
