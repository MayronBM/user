package ni.com.user.security.mapper;

import ni.com.user.security.dto.SignUpDto;
import ni.com.user.security.dto.UserRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {PhoneMapper.class}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SignUpMapper {
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "phones", target = "phones")
    UserRequestDto convertToUserRequest(SignUpDto userDto);
}
