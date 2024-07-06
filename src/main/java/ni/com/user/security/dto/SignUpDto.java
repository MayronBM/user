package ni.com.user.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ni.com.user.security.support.annotation.password.Extended;
import ni.com.user.security.support.annotation.password.Password;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpDto implements Serializable {
    @NotBlank(message = "{userName.notBlank}")
    @Size(min = 3, message = "{userName.minSize}")
    @Size(max = 20, message = "{userName.maxSize}")
    private String username;
    @Email(message = "{email.format}", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    @NotBlank(message = "{email.notBlank}")
    private String email;
    @NotBlank(message = "{password.notBlank}")
    @Password(groups = Extended.class)
    private String password;
    @Valid
    private List<PhoneDto> phones;
}
