package ni.com.user.security.mapper;

import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.model.User;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(
        componentModel = "spring",
        uses = {PhoneMapper.class},
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name", ignore = true)
    @Mapping(target = "email", source = "user.email", ignore = true)
    @Mapping(target = "password", source = "user.password", ignore = true)
    @Mapping(target = "created", source = "user.created")
    @Mapping(target = "modified", source = "user.modified")
    @Mapping(target = "lastLogin", source = "user.lastLogin")
    @Mapping(target = "token", source = "user.token")
    @Mapping(target = "isactive", source = "user.enabled")
    @Mapping(target = "phones", source = "user.phones", ignore = true)
    UserResponseDto convert(User user);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "isactive", target = "enabled")
    @Mapping(source = "phones", target = "phones")
    User convertir(UserResponseDto userResponseDto);

    @Named(value = "users")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "password", source = "user.password", ignore = true)
    @Mapping(target = "created", source = "user.created")
    @Mapping(target = "modified", source = "user.modified")
    @Mapping(target = "lastLogin", source = "user.lastLogin")
    @Mapping(target = "token", source = "user.token", ignore = true)
    @Mapping(target = "isactive", source = "user.enabled")
    @Mapping(target = "phones", source = "user.phones")
    UserResponseDto convertTo(User user);

    @IterableMapping(qualifiedByName = "users")
    List<UserResponseDto> map(List<User> userList);
}
