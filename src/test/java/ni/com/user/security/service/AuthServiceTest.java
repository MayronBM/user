package ni.com.user.security.service;

import ni.com.user.security.dto.SignInDto;
import ni.com.user.security.dto.SignUpDto;
import ni.com.user.security.dto.UserRequestDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.mapper.SignUpMapper;
import ni.com.user.security.mapper.UserMapper;
import ni.com.user.security.model.User;
import ni.com.user.security.support.security.UserDetailsImpl;
import ni.com.user.security.support.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserMapper userMapper;

    @Mock
    private SignUpMapper signUpMapper;

    @InjectMocks
    private AuthService authService;
    private User user;
    private String token;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken("prueba@prueba.com", "Prueba*123");

        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcnVlYmFAcHJ1ZWJhMi5jb20iLCJpYXQiOjE3MTU2MjI2MzksImV4cCI6MTcxNTYyMzIzOX0.BAgcgAhT64n1em1QwbOQbP9i6NdUUBWJfbSYuLr9T7I";

        user = User.builder()
                .id(UUID.fromString("bef39ea1-3e0a-4189-8864-343f32875f62"))
                .username("prueba")
                .email("prueba@prueba.com")
                .password("Prueba*123")
                .created(LocalDateTime.now())
                .modificated(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token(token)
                .enabled(true)
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(UUID.fromString("bef39ea1-3e0a-4189-8864-343f32875f62"))
                .username("prueba")
                .email("prueba@prueba.com")
                .password("Prueba*123")
                .roles(new ArrayList<>())
                .created(LocalDateTime.now())
                .modificated(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .token(token)
                .isactive(true).build();

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Mockito.when(authenticationManager.authenticate(authToken)).thenReturn(auth);
        Mockito.when(jwtUtils.generateJwtToken(auth)).thenReturn(token);
        Mockito.when(userService.findByEmailOrUsername("prueba@prueba.com", "prueba@prueba.com"))
                .thenReturn(user);
        Mockito.when(userService.save(user)).thenReturn(user);
        Mockito.when(userMapper.convert(user)).thenReturn(userResponseDto);
    }

    @Test
    public void signInUserTest() {

        SignInDto signInDto = new SignInDto("prueba@prueba.com", "Prueba*123");
        UserResponseDto userResponse = authService.signInUser(signInDto);

        assertEquals(userResponse.getUsername(), user.getUsername());
        assertEquals(userResponse.getToken(), token);
    }

    @Test
    public void signUpUserTest() {
        SignUpDto signUpDto = SignUpDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("Prueba123*")
                .build();

        UserRequestDto userRequestDto = UserRequestDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("Prueba123*")
                .build();

        UserResponseDto userResponseDto = UserResponseDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("Prueba123*")
                .build();

        Mockito.when(signUpMapper.convertToUserRequest(signUpDto)).thenReturn(userRequestDto);
        Mockito.when(userService.save(userRequestDto)).thenReturn(userResponseDto);

        UserResponseDto value = authService.signUpUser(signUpDto);
        assertEquals(value.getUsername(), userResponseDto.getUsername());

    }
}
