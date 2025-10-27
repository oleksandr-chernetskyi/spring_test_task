package authapi.servicea.security;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import authapi.servicea.dto.user.UserLoginRequestDto;
import authapi.servicea.dto.user.UserLoginResponseDto;
import authapi.servicea.exception.AuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserLoginRequestDto userLoginRequestDto;
    private Authentication authenticationMock;

    @BeforeEach
    void setUp() {
        userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("bob@mail.com");
        userLoginRequestDto.setPassword("encodedPassword");

        authenticationMock = Mockito.mock(Authentication.class);
    }

    @Test
    @DisplayName("authenticate_ValidCredentials_ReturnsJwtToken")
    void authenticateValidCredentialsReturnsJwtToken() {
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getName()).thenReturn(userLoginRequestDto.getEmail());
        Mockito.when(jwtUtil.generateToken(userLoginRequestDto.getEmail()))
                .thenReturn("valid-mocked-jwt-token");

        UserLoginResponseDto userLoginResponseDto = authenticationService
                .authenticate(userLoginRequestDto);

        assertThat(userLoginResponseDto).isNotNull();
        assertThat(userLoginResponseDto.token()).isEqualTo("valid-mocked-jwt-token");

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtUtil).generateToken(userLoginRequestDto.getEmail());
    }

    @Test
    @DisplayName("authenticate_TokenGenerationFails_ThrowsAuthenticationException")
    void authenticateTokenGenerationFailsThrowsAuthenticationException () {
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticationMock);
        Mockito.when(authenticationMock.getName()).thenReturn(userLoginRequestDto.getEmail());
        Mockito.when(jwtUtil.generateToken(userLoginRequestDto.getEmail()))
                .thenReturn("");

        assertThatThrownBy(() -> authenticationService
                .authenticate(userLoginRequestDto))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Authentication failed");

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtUtil).generateToken(userLoginRequestDto.getEmail());
    }
}
