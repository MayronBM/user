package ni.com.user.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ni.com.user.security.support.annotation.password.Extended;
import ni.com.user.security.support.annotation.password.Password;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"password"})
public class UserUpdateDto implements Serializable {
    @NotBlank(message = "{name.notBlank}")
    @Size(min = 10, message = "{name.minSize}")
    @Size(max = 100, message = "{name.maxSize}")
    private String name;
    @NotBlank(message = "{password.notBlank}")
    @Password(groups = Extended.class)
    private String password;
    private Boolean isactive;
    @Valid
    private List<PhoneDto> phones;
}
