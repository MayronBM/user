package ni.com.user.security.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.dto.UserUpdateDto;
import ni.com.user.security.mapper.PhoneMapper;
import ni.com.user.security.mapper.UserCreateMapper;
import ni.com.user.security.mapper.UserMapper;
import ni.com.user.security.model.Phone;
import ni.com.user.security.model.User;
import ni.com.user.security.repository.UserRepository;
import ni.com.user.security.service.UserService;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MessageResource messageResource;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserCreateMapper userCreateMapper;
    private final PhoneMapper phoneMapper;

    /**
     * Método que permite obtener un usuario por medio del email.
     *
     * @param email parámetro de búsqueda.
     * @return usuario encontrado.
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageResource.getMessage("error.userNotFound") + email));
    }

    /**
     * Método que permite obtener todos los usuarios.
     *
     * @return Lista de usuarioResponseDto;
     */
    @Override
    public List<UserResponseDto> findAll() {
        return userMapper.map(userRepository.findAll());
    }

    /**
     * Método que permite obtener un usuario por medio de su id.
     *
     * @param id parámetro de búsqueda.
     * @return usuario encontrado.
     */
    @Override
    public UserResponseDto findById(UUID id) {
        return userMapper.convertTo(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageResource.getMessage("error.EntityNotFoundException"))));
    }

    /**
     * Método que permite guardar una entidad User.
     *
     * @param user Usuario a guardar.
     * @return usuario guardado.
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Método que permite guardar un nuevo usuario, agregando algunas propiedades necesarias en el proceso.
     *
     * @param userDto Parámetro a guardar.
     * @return usuario guardado.
     */
    @Override
    public UserResponseDto saveDto(UserCreateDto userDto) {
        final User user = userCreateMapper.convert(userDto);

        user.getPhones().forEach(phone -> phone.setUser(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());

        return userMapper.convert(this.save(user));
    }

    /**
     * Método que permite actualizar los datos de un usuario.
     *
     * @param userUpdateDto datos del usuario a modificar.
     * @param id            id del usuario.
     * @return Usuario actualizado.
     */
    @Override
    public UserResponseDto updateDto(UserUpdateDto userUpdateDto, UUID id) {

        final User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageResource.getMessage("error.EntityNotFoundException")));

        List<Phone> phones = phoneMapper.map(userUpdateDto.getPhones());
        phones.forEach(phone -> phone.setUser(user));

        user.setName(userUpdateDto.getName());
        user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        user.setEnabled(userUpdateDto.getIsactive() != null ? userUpdateDto.getIsactive() : true);
        user.setPhones(phones);
        user.setModified(LocalDateTime.now());

        return userMapper.convertTo(this.save(user));
    }

    /**
     * Método que permite eliminar un usuario.
     *
     * @param id id del usuario a eliminar.
     */
    @Override
    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageResource.getMessage("error.EntityNotFoundException")));

        userRepository.delete(user);
    }
}
