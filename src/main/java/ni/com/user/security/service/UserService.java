package ni.com.user.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.UserRequestDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.mapper.PhoneMapper;
import ni.com.user.security.mapper.UserMapper;
import ni.com.user.security.mapper.UserRequestMapper;
import ni.com.user.security.model.Phone;
import ni.com.user.security.model.User;
import ni.com.user.security.repository.UserRepository;
import ni.com.user.security.support.exception.ValueAlreadyExistsException;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MessageResource messageResource;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRequestMapper userRequestMapper;
    private final PhoneMapper phoneMapper;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmailOrUsername(String email, String username) {
        return userRepository.findByEmailOrUsername(email, username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageResource.getMessage("error.userNotFound") + email));
    }

    public List<UserResponseDto> findAll() {
        return userMapper.map(userRepository.findAll());
    }

    public UserResponseDto findById(String id) {
        return userMapper.convertTo(userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(
                        messageResource.getMessage("error.EntityNotFoundException"))));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserResponseDto save(UserRequestDto userDto) throws ValueAlreadyExistsException {

        User user = userRequestMapper.convert(userDto);

        User finalUser = user;
        user.getPhones().forEach(phone -> phone.setUser(finalUser));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());
        user = this.save(user);

        return userMapper.convert(user);
    }

    public UserResponseDto update(UserRequestDto userRequestDto, String id) {

        UUID uuid = UUID.fromString(id);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageResource.getMessage("error.EntityNotFoundException")));

        List<Phone> phones = phoneMapper.map(userRequestDto.getPhones());
        User finalUser = user;
        phones.forEach(phone -> phone.setUser(finalUser));

        user.setId(uuid);
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setEnabled(userRequestDto.getIsactive() != null ? userRequestDto.getIsactive() : true);
        user.setPhones(phones);
        user.setModificated(LocalDateTime.now());
        user = this.save(user);

        return userMapper.convertTo(user);
    }

    public void delete(String id) {
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException(
                messageResource.getMessage("error.EntityNotFoundException")));

        userRepository.delete(user);
    }
}
