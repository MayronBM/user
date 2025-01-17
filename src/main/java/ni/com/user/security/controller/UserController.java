package ni.com.user.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.ApiResponseDto;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.dto.UserUpdateDto;
import ni.com.user.security.service.UserService;
import ni.com.user.security.support.annotation.password.Sequence;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Administración de usuarios")
public class UserController {

    private final MessageResource messageResource;
    private final UserService userService;

    @Operation(summary = "Lista todos los usuarios.")
    @GetMapping("/")
    public ResponseEntity<ApiResponseDto<?>> findAll() {
        List<UserResponseDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(users)
                        .message(messageResource.getMessage("users.list"))
                        .build());
    }

    @Operation(summary = "Obtiene usuario por id.")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(userService.findById(id))
                        .message(messageResource.getMessage("users.byId"))
                        .build());
    }

    @Operation(summary = "Guarda un nuevo usuario.")
    @PostMapping("/")
    public ResponseEntity<ApiResponseDto<?>> save(@RequestBody @Validated(Sequence.class) UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(userService.saveDto(userCreateDto))
                        .message(messageResource.getMessage("success.created"))
                        .build());
    }

    @Operation(summary = "Modifica un registro de usuario.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> update(@RequestBody @Validated(Sequence.class) UserUpdateDto userUpdateDto
            , @PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .response(userService.updateDto(userUpdateDto, id))
                        .message(messageResource.getMessage("success.updated"))
                        .build());
    }

    @Operation(summary = "Elimina un registro de usuario.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<?>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                ApiResponseDto.builder()
                        .isSuccess(true)
                        .message(messageResource.getMessage("success.deleted"))
                        .build());
    }
}
