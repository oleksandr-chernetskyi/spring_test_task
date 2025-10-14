package authapi.servicea.service.impl;

import authapi.servicea.dto.user.UserRegistrationRequestDto;
import authapi.servicea.dto.user.UserResponseDto;
import authapi.servicea.exception.EntityNotFoundException;
import authapi.servicea.exception.RegistrationException;
import authapi.servicea.mapper.UserMapper;
import authapi.servicea.model.User;
import authapi.servicea.repository.UserRepository;
import authapi.servicea.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(registrationRequestDto.getEmail()).isPresent()) {
            log.error("Register method failed. "
                            + "Unable to registration with current parameters: {}",
                    registrationRequestDto.getEmail());
            throw new RegistrationException("Unable to complete registration");
        }
        User user = new User();
        user.setEmail(registrationRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequestDto.getPassword()));
        user.setFirstName(registrationRequestDto.getFirstName());
        user.setLastName(registrationRequestDto.getLastName());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> {
                    log.error("GetAuthenticatedUser method failed. "
                            + "Can't find user by email: {}", authentication.getName());
                    return new EntityNotFoundException("Can't find user by email: "
                            + authentication.getName());
                });
    }
}
