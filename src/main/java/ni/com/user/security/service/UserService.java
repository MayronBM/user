package ni.com.user.security.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ni.com.user.security.dto.UserCreateDto;
import ni.com.user.security.dto.UserUpdateDto;
import ni.com.user.security.dto.UserResponseDto;
import ni.com.user.security.mapper.PhoneMapper;
import ni.com.user.security.mapper.UserCreateMapper;
import ni.com.user.security.mapper.UserMapper;
import ni.com.user.security.mapper.UserUpdateMapper;
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
    private final UserUpdateMapper userUpdateMapper;
    private final UserCreateMapper userCreateMapper;
    private final PhoneMapper phoneMapper;

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        messageResource.getMessage("error.userNotFound") + email));
    }

    public List<UserResponseDto> findAll() {
        return userMapper.map(userRepository.findAll());
    }

    public UserResponseDto findById(UUID id) {
        return userMapper.convertTo(userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageResource.getMessage("error.EntityNotFoundException"))));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public UserResponseDto save(UserCreateDto userDto) throws ValueAlreadyExistsException {
        final User user = userCreateMapper.convert(userDto);

        user.getPhones().forEach(phone -> phone.setUser(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());

        return userMapper.convert(this.save(user));
    }

    public UserResponseDto update(UserUpdateDto userUpdateDto, UUID id) {

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

    public void delete(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageResource.getMessage("error.EntityNotFoundException")));

        userRepository.delete(user);
    }
}
