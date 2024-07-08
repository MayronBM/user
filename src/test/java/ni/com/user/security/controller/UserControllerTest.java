package ni.com.user.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ni.com.user.security.dto.PhoneDto;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.dto.UserUpdateDto;
import ni.com.user.security.service.impl.UserServiceImpl;
import ni.com.user.security.support.message.MessageResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@MockBean(SecurityFilterChain.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageResource messageResource;
    @MockBean
    private UserServiceImpl userServiceImpl;
    private UserUpdateDto userUpdateDto;
    private UserCreateDto userCreateDto;
    private UserResponseDto userResponseDto;
    private final String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcnVlYmFAcHJ1ZWJhMi5jb20iLCJpYXQiOjE3MTU2MjI2MzksImV4cCI6MTcxNTYyMzIzOX0.BAgcgAhT64n1em1QwbOQbP9i6NdUUBWJfbSYuLr9T7I";
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

        userUpdateDto = UserUpdateDto.builder()
                .name("prueba prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        userCreateDto = UserCreateDto.builder()
                .email("prueba@prueba.com")
                .name("prueba prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();

        userResponseDto = UserResponseDto.builder()
                .id(UUID.fromString("bef39ea1-3e0a-4189-8864-343f32875f62"))
                .email("prueba@prueba.com")
                .name("prueba prueba")
                .password("Prueba123*")
                .phones(Collections.singletonList(phoneDto))
                .build();
    }

    @Test
    public void findAllTest() throws Exception {
        String message = "Lista De usuarios.";

        Mockito.when(messageResource.getMessage("users.list"))
                .thenReturn(message);
        Mockito.when(userServiceImpl.findAll()).thenReturn(Collections.singletonList(userResponseDto));

        ResultActions resultActions = mockMvc.perform(get("/users/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.response.size()", is(1)));
    }

    @Test
    public void findByIdTest() throws Exception {
        String message = "Búsqueda usuario por id.";
        String idString = "bef39ea1-3e0a-4189-8864-343f32875f62";
        UUID id = UUID.fromString(idString);

        Mockito.when(messageResource.getMessage("users.byId"))
                .thenReturn(message);

        Mockito.when(userServiceImpl.findById(id)).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(get("/users/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.response.id", is(idString)));
    }

    @Test
    public void saveFailTest() throws Exception {
        String message = "Datos no válidos.";
        String messagePassword = "Contraseña debe tener al menos 10 caracteres.";
        userCreateDto.setPassword("1234");

        Mockito.when(messageResource.getMessage("error.argumentNotValid"))
                .thenReturn(message);

        ResultActions resultActions = mockMvc.perform(post("/users/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto))
                .with(csrf()));

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.response[0]", is(messagePassword)));
    }

    @Test
    public void saveTest() throws Exception {
        String message = "Registro agregado exitosamente.";

        Mockito.when(messageResource.getMessage("success.created"))
                .thenReturn(message);


        Mockito.when(userServiceImpl.saveDto(any(UserCreateDto.class))).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(post("/users/")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto))
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.response.id", is("bef39ea1-3e0a-4189-8864-343f32875f62")));
    }

    @Test
    public void updateTest() throws Exception {
        String message = "Registro actualizado exitosamente.";
        String idString = "bef39ea1-3e0a-4189-8864-343f32875f62";
        UUID id = UUID.fromString(idString);
        userResponseDto.setId(id);

        Mockito.when(messageResource.getMessage("success.updated"))
                .thenReturn(message);

        Mockito.when(userServiceImpl.updateDto(any(UserUpdateDto.class), any(UUID.class))).thenReturn(userResponseDto);

        ResultActions resultActions = mockMvc.perform(put("/users/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateDto))
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)))
                .andExpect(jsonPath("$.response.id", is(idString)));
    }

    @Test
    public void deleteTest() throws Exception {
        String message = "Registro eliminado exitosamente.";
        UUID id = UUID.fromString("bef39ea1-3e0a-4189-8864-343f32875f62");

        Mockito.when(messageResource.getMessage("success.deleted"))
                .thenReturn(message);

        Mockito.doNothing().when(userServiceImpl).delete(id);

        ResultActions resultActions = mockMvc.perform(delete("/users/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(message)));
    }
}
