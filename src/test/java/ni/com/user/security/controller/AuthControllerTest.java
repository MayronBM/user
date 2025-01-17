package ni.com.user.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ni.com.user.security.dto.*;
import ni.com.user.security.service.impl.AuthServiceImpl;
import ni.com.user.security.service.impl.UserServiceImpl;
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
    private AuthServiceImpl authServiceImpl;
    @MockBean
    private UserServiceImpl userServiceImpl;
    @MockBean
    private MessageResource messageResource;
    private UserCreateDto userCreateDto;
    private UserResponseDto userResponseDto;
    private SignInDto sign;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        PhoneDto phoneDto = PhoneDto.builder()
                .cityCode("123")
                .contryCode("123")
                .number("12345678")
                .build();

        userCreateDto = UserCreateDto.builder()
                .email("prueba@prueba.com")
                .name("prueba prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        userResponseDto = UserResponseDto.builder()
                .email("prueba@prueba.com")
                .name("prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        sign = SignInDto.builder()
                .email(userCreateDto.getEmail())
                .password(userCreateDto.getPassword())
                .build();
    }

    @Test
    public void emailNull() throws Exception {
        userCreateDto = UserCreateDto.builder()
                .name("prueba")
                .password("12345678")
                .build();

        String message = "Datos no válidos.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(userCreateDto)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void usernameNull() throws Exception {
        userCreateDto = UserCreateDto.builder()
                .email("prueba@prueba.com")
                .password("12345678")
                .build();

        String message = "Datos no válidos.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(userCreateDto)));

        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void passwordNull() throws Exception {
        userCreateDto = UserCreateDto.builder()
                .email("prueba@prueba.com")
                .name("prueba")
                .build();

        String message = "Datos no válidos.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(userCreateDto)));

        resultActions.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void insecurePassword() throws Exception {
        userCreateDto = UserCreateDto.builder()
                .email("prueba@prueba.com")
                .name("prueba prueba")
                .password("12347s")
                .build();

        String message = "Datos no válidos.";
        String messagePassword = "Contraseña debe tener al menos 10 caracteres.";

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(userCreateDto)));

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

        Mockito.when(userServiceImpl.saveDto(userCreateDto)).thenReturn(userResponseDto);

        Mockito.when(authServiceImpl.signInUser(sign)).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(userCreateDto)));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void badPasswordTest() throws Exception {
        String message = "Inicio de sesión exitoso.";

        Mockito.when(messageResource.getMessage("success.signin"))
                .thenReturn(message);

        Mockito.when(authServiceImpl.signInUser(sign)).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .content(objectMapper.writeValueAsString(sign)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)));
    }
}
