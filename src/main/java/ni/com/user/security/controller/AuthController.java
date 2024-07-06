package ni.com.user.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.*;
import ni.com.user.security.service.AuthService;
import ni.com.user.security.service.UserService;
import ni.com.user.security.support.annotation.password.Sequence;
import ni.com.user.security.support.exception.RoleNotFoundException;
import ni.com.user.security.support.exception.ValueAlreadyExistsException;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Dar de alta y autenticación de usuario")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final MessageResource messageResource;

    @Operation(summary = "Registra un nuevo usuario e inicia sesión.")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponseDto<?>> registerUser(@RequestBody @Validated(Sequence.class) SignUpDto userDto)
            throws ValueAlreadyExistsException, RoleNotFoundException {
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