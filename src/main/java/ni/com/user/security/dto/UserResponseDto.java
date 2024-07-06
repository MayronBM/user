package ni.com.user.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ni.com.user.security.support.annotation.password.Password;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto implements Serializable {
    private UUID id;
    @NotBlank(message = "{userName.notBlank}")
    @Size(min = 3, message = "{userName.minSize}")
    @Size(max = 20, message = "{userName.maxSize}")
    private String username;
    @Email(message = "{email.format}", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "{email.notBlank}")
    private String email;
    @NotBlank(message = "{password.notBlank}")
    @Password
    private String password;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modificated;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;
    private String token;
    private boolean isactive;
    private List<String> roles;
    @Valid
    private List<PhoneDto> phones;
}
