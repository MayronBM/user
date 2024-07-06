package ni.com.user.security.support.security;

import lombok.RequiredArgsConstructor;
import ni.com.user.security.repository.UserRepository;
import ni.com.user.security.support.message.MessageResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final MessageResource messageResource;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return UserDetailsImpl.build(userRepository.findByUsername(username)
                .orElse(userRepository.findByEmail(username).orElseThrow
                        (() -> new UsernameNotFoundException(messageResource.getMessage("error.userNotFound") + username))));
    }
}
