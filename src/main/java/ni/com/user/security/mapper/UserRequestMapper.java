package ni.com.user.security.mapper;

import ni.com.user.security.dto.UserRequestDto;
import ni.com.user.security.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {PhoneMapper.class}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserRequestMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "isactive", target = "enabled")
    @Mapping(source = "phones", target = "phones")
    User convert(UserRequestDto userDto);
}
