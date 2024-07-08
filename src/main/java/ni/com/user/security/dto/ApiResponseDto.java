package ni.com.user.security.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponseDto<T> {
    private boolean isSuccess;
    private String message;
    private T response;
}
