package ni.com.user.security.service;

import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.dto.UserUpdateDto;
import ni.com.user.security.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User findByEmail(String email);

    List<UserResponseDto> findAll();

    UserResponseDto findById(UUID id);

    User save(User user);

    UserResponseDto saveDto(UserCreateDto userDto);

    UserResponseDto updateDto(UserUpdateDto userUpdateDto, UUID id);

    void delete(UUID id);
}
