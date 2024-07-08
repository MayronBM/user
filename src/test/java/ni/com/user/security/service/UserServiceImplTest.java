package ni.com.user.security.service;

import jakarta.persistence.EntityNotFoundException;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.dto.UserUpdateDto;
import ni.com.user.security.mapper.PhoneMapper;
import ni.com.user.security.mapper.UserCreateMapper;
import ni.com.user.security.mapper.UserMapper;
import ni.com.user.security.mapper.UserUpdateMapper;
import ni.com.user.security.model.User;
import ni.com.user.security.repository.UserRepository;
import ni.com.user.security.service.impl.UserServiceImpl;
import ni.com.user.security.support.message.MessageResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageResource messageResource;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PhoneMapper phoneMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserUpdateMapper userUpdateMapper;
    @Mock
    private UserCreateMapper userCreateMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;
    private User user;
    private UserResponseDto userResponseDto;
    private UserUpdateDto userUpdateDto;
    private UserCreateDto userCreateDto;
    private final UUID id = UUID.fromString("bef39ea1-3e0a-4189-8864-343f32875f62");

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(id)
                .name("prueba prueba")
                .email("prueba@prueba.com")
                .password("Prueba*123")
                .phones(new ArrayList<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .enabled(true)
                .build();

        userResponseDto = UserResponseDto.builder()
                .id(id)
                .name("prueba prueba")
                .email("prueba@prueba.com")
                .password("Prueba*123")
                .roles(new ArrayList<>())
                .phones(new ArrayList<>())
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isactive(true)
                .build();

        userUpdateDto = UserUpdateDto.builder()
                .name("prueba prueba")
                .password("Prueba*123")
                .phones(new ArrayList<>())
                .isactive(true)
                .build();

        userCreateDto = UserCreateDto.builder()
                .name("prueba prueba")
                .email("prueba@prueba.com")
                .password("Prueba*123")
                .phones(new ArrayList<>())
                .build();
    }

    @Test
    public void findByEmailTest() {
        String email = "prueba@prueba.com";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User value = userServiceImpl.findByEmail(email);

        assertNotNull(value);
        assertEquals(value.getName(), user.getName());

    }

    @Test()
    public void findByEmailOrUsernameFailTest() {
        String email = "prueba";

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(messageResource.getMessage("error.userNotFound")).thenReturn("Registro no encontrado con nombre de usuario: ");

        UsernameNotFoundException thrown = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userServiceImpl.findByEmail(email);
        }, "UsernameNotFoundException was expected");

        Assertions.assertEquals("Registro no encontrado con nombre de usuario: prueba", thrown.getMessage());
    }

    @Test
    public void findAll() {
        User user = new User();
        List<User> userList = List.of(user);
        List<UserResponseDto> userResponseDtoList = List.of(userResponseDto);

        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userMapper.map(userList)).thenReturn(userResponseDtoList);

        List<UserResponseDto> list = userServiceImpl.findAll();

        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    public void findByIdTest() {
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Mockito.when(userMapper.convertTo(user)).thenReturn(userResponseDto);

        UserResponseDto userDto = userServiceImpl.findById(id);

        assertEquals(userDto.getEmail(), userResponseDto.getEmail());
    }

    @Test()
    public void findByIdFailTest() {
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.when(messageResource.getMessage("error.EntityNotFoundException"))
                .thenReturn("No se encontró coincidencias.");

        EntityNotFoundException thrown = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userServiceImpl.findById(id);
        }, "EntityNotFoundException was expected");

        Assertions.assertEquals("No se encontró coincidencias.", thrown.getMessage());
    }

    @Test
    public void saveTest() {
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User userResponse = userServiceImpl.save(user);

        assertEquals(user.getEmail(), userResponse.getEmail());
    }

    @Test
    public void saveDto() {
        Mockito.when(userCreateMapper.convert(userCreateDto)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(userUpdateDto.getPassword())).thenReturn(userUpdateDto.getPassword());
        Mockito.when(userMapper.convert(user)).thenReturn(userResponseDto);
        Mockito.when(userServiceImpl.save(user)).thenReturn(user);

        UserResponseDto responseDto = userServiceImpl.saveDto(userCreateDto);

        assertNotNull(responseDto);
        assertEquals(id, responseDto.getId());

    }

    @Test
    public void updateDtoEntityNotFoundTest() {
        Mockito.when(messageResource.getMessage("error.EntityNotFoundException"))
                .thenReturn("No se encontró coincidencias.");

        EntityNotFoundException thrownUser = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userServiceImpl.updateDto(userUpdateDto, id);
        }, "EntityNotFoundException was expected");

        assertEquals("No se encontró coincidencias.", thrownUser.getMessage());
    }

    @Test
    public void updateDtoTest() {
        Mockito.when(userUpdateMapper.convert(userUpdateDto)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(userUpdateDto.getPassword())).thenReturn(userUpdateDto.getPassword());
        Mockito.when(userMapper.convertTo(user)).thenReturn(userResponseDto);
        Mockito.when(phoneMapper.map(userUpdateDto.getPhones())).thenReturn(new ArrayList<>());
        Mockito.when(userServiceImpl.save(user)).thenReturn(user);
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDto responseDto = userServiceImpl.updateDto(userUpdateDto, id);

        assertNotNull(responseDto);
        assertEquals(id, responseDto.getId());
        assertEquals(userUpdateDto.getName(), user.getName());

    }

    @Test
    public void deleteEntityNotFound() {
        Mockito.when(messageResource.getMessage("error.EntityNotFoundException"))
                .thenReturn("No se encontró coincidencias.");

        EntityNotFoundException thrownUser = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userServiceImpl.delete(id);
        }, "EntityNotFoundException was expected");

        assertEquals("No se encontró coincidencias.", thrownUser.getMessage());
    }

    @Test
    public void deleteTest() {
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userRepository).delete(user);
        userServiceImpl.delete(id);

        assertAll(() -> userServiceImpl.delete(id));

    }
}
