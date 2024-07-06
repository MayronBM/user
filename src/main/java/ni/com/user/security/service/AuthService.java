package ni.com.user.security.service;

import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.SignInDto;
import ni.com.user.security.dto.SignUpDto;
import ni.com.user.security.dto.UserRequestDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.mapper.SignUpMapper;
import ni.com.user.security.mapper.UserMapper;
import ni.com.user.security.model.User;
import ni.com.user.security.support.security.jwt.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final SignUpMapper signUpMapper;

    public UserResponseDto signUpUser(SignUpDto signUpDto) {
        UserRequestDto user = signUpMapper.convertToUserRequest(signUpDto);
        userService.save(user);
        return signInUser(new SignInDto(user.getEmail(), user.getPassword()));
    }

    public UserResponseDto signInUser(SignInDto signInRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.findByEmailOrUsername(signInRequestDto.getEmail(), signInRequestDto.getEmail());

        user.setModificated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken(jwtUtils.generateJwtToken(authentication));
        userService.save(user);

        return userMapper.convert(user);
    }
}
