package authapi.servicea.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import authapi.servicea.dto.user.UserRegistrationRequestDto;
import authapi.servicea.dto.user.UserResponseDto;
import authapi.servicea.exception.RegistrationException;
import authapi.servicea.mapper.UserMapper;
import authapi.servicea.model.User;
import authapi.servicea.repository.UserRepository;
import authapi.servicea.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationRequestDto userRegistrationRequestDto;
    private User testUser;
    private UserResponseDto testUserResponseDto;

    @BeforeEach
    void setUp() {
        userRegistrationRequestDto = new UserRegistrationRequestDto();
        userRegistrationRequestDto.setEmail("alice@mail.com");
        userRegistrationRequestDto.setPassword("rawPassword");
        userRegistrationRequestDto.setRepeatPassword("rawPassword");
        userRegistrationRequestDto.setFirstName("Alice");
        userRegistrationRequestDto.setLastName("Alison");

        testUser = new User();
        testUser.setId(UUID.randomUUID());
        testUser.setEmail("alice@mail.com");
        testUser.setPassword("encodedPassword");
        testUser.setFirstName("Alice");
        testUser.setLastName("Alison");

        testUserResponseDto = new UserResponseDto();
        testUserResponseDto.setId(testUser.getId());
        testUserResponseDto.setEmail(testUser.getEmail());
    }

    @Test
    @DisplayName("register_NewUser_ReturnsUserResponseDto")
    void registerNewUserReturnsUserResponseDto() throws RegistrationException {
        when(userRepository.findByEmail(userRegistrationRequestDto.getEmail()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRegistrationRequestDto.getPassword()))
                .thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDto(testUser)).thenReturn(testUserResponseDto);

        UserResponseDto result = userService.register(userRegistrationRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("alice@mail.com");

        verify(userRepository).findByEmail(userRegistrationRequestDto.getEmail());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(userRegistrationRequestDto.getPassword());
        verify(userMapper).toDto(testUser);
    }

    @Test
    @DisplayName("register_EmailAlreadyExists_ThrowsRegistrationException")
    void registerEmailAlreadyExistsThrowsRegistrationException() {
        when(userRepository.findByEmail(userRegistrationRequestDto.getEmail()))
                .thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService
                .register(userRegistrationRequestDto))
                .isInstanceOf(RegistrationException.class)
                .hasMessageContaining("Unable to complete registration");

        verify(userRepository).findByEmail(userRegistrationRequestDto.getEmail());
        verify(userRepository, never()).save(any());
    }





}