package ni.com.user.security.service;

import ni.com.user.security.dto.SignInDto;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;

public interface AuthService {
    UserResponseDto signUpUser(UserCreateDto userCreateDto);

    UserResponseDto signInUser(SignInDto signInRequestDto);
}
