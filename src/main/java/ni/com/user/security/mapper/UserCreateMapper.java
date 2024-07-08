package ni.com.user.security.mapper;

import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = {PhoneMapper.class}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserCreateMapper {
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "phones", target = "phones")
    User convert(UserCreateDto userDto);
}
