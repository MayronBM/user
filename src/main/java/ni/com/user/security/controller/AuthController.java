package ni.com.user.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.ApiResponseDto;
import ni.com.user.security.dto.SignInDto;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.service.AuthService;
import ni.com.user.security.support.annotation.password.Sequence;
import ni.com.user.security.support.exception.ValueAlreadyExistsException;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Dar de alta y autenticación de usuario")
public class AuthController {

    private final AuthService authService;
    private final MessageResource messageResource;

    @Operation(summary = "Registra un nuevo usuario e inicia sesión.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<?>> registerUser(@RequestBody @Validated(Sequence.class) UserCreateDto userDto)
            throws ValueAlreadyExistsException {
        UserResponseDto user = authService.signUpUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(user)
                        .message(messageResource.getMessage("success.created"))
                        .build());
    }

    @Operation(summary = "Inicia sesión.")
    @PostMapping("/signin")
    public ResponseEntity<ApiResponseDto<?>> signinUser(@RequestBody @Valid SignInDto userDto) {
        UserResponseDto user = authService.signInUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(user)
                        .message(messageResource.getMessage("success.signin"))
                        .build());
    }

}
