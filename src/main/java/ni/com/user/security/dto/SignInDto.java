package ni.com.user.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class SignInDto implements Serializable {
    @NotBlank(message = "{email.notBlank}")
    private String email;

    @NotBlank(message = "{password.notBlank}")
    private String password;
}
