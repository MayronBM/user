package ni.com.user.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ni.com.user.security.dto.*;
import ni.com.user.security.service.AuthService;
import ni.com.user.security.service.UserService;
import ni.com.user.security.support.message.MessageResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@MockBean(SecurityFilterChain.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthService authService;
    @MockBean
    private UserService userService;
    @MockBean
    private MessageResource messageResource;
    private SignUpDto signUpDto;
    private UserResponseDto userResponseDto;
    private SignInDto sign;
    @Autowired
    private ObjectMapper objectMapper;
    private UserRequestDto userRequestDto;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        PhoneDto phoneDto = PhoneDto.builder()
                .cityCode("123")
                .contryCode("123")
                .number("12345678")
                .build();

        signUpDto = SignUpDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        userRequestDto = UserRequestDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        userResponseDto = UserResponseDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        sign = SignInDto.builder()
                .email(signUpDto.getEmail())
                .password(signUpDto.getPassword())
                .build();
    }

    @Test
    public void emailNull() throws Exception {
        signUpDto = SignUpDto.builder()
                .username("prueba")
                .password("12345678")
                .build();

        String message = "Datos no válidos.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(signUpDto)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void usernameNull() throws Exception {
        signUpDto = SignUpDto.builder()
                .email("prueba@prueba.com")
                .password("12345678")
                .build();

        String message = "Datos no válidos.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(signUpDto)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void passwordNull() throws Exception {
        signUpDto = SignUpDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .build();

        String message = "Datos no válidos.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(signUpDto)));

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void insecurePassword() throws Exception {
        signUpDto = SignUpDto.builder()
                .email("prueba@prueba.com")
                .username("prueba")
                .password("12347s")
                .build();

        String message = "Datos no válidos.";
        String messagePassword = "Contraseña debe tener al menos 10 caracteres.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(signUpDto)));

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.response[0]", is(messagePassword)));
    }

    @Test
    public void registerUserTest() throws Exception {
        String message = "Registro agregado exitosamente.";


        Mockito.when(messageResource.getMessage("success.created"))
                .thenReturn(message);

        Mockito.when(userService.save(userRequestDto)).thenReturn(userResponseDto);

        Mockito.when(authService.signInUser(sign)).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(signUpDto)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void badPasswordTest() throws Exception {
        String message = "Inicio de sesión exitoso.";

        Mockito.when(messageResource.getMessage("success.signin"))
                .thenReturn(message);

        Mockito.when(authService.signInUser(sign)).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(sign)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)));
    }
}
