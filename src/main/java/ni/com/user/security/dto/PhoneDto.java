package ni.com.user.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDto implements Serializable {
    private UUID id;
    @NotBlank(message = "{number.notBlank}")
    @Pattern(regexp = "\\d{1,8}$", message = "{number.pattern}")
    @Size(min = 8, message = "{number.minSize}")
    @Size(max = 8, message = "{number.maxSize}")
    private String number;
    @NotBlank(message = "{cityCode.notNull}")
    @Pattern(regexp = "\\d{1,10}$", message = "{cityCode.pattern}")
    @Size(min = 1, message = "{cityCode.minSize}")
    @Size(max = 10, message = "{cityCode.maxSize}")
    private String cityCode;
    @NotBlank(message = "{contryCode.notNull}")
    @Pattern(regexp = "\\d{1,10}$", message = "{contryCode.pattern}")
    @Size(min = 1, message = "{contryCode.minSize}")
    @Size(max = 10, message = "{contryCode.maxSize}")
    private String contryCode;
}
