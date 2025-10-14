package authapi.servicea.security;

import authapi.servicea.dto.user.UserLoginRequestDto;
import authapi.servicea.dto.user.UserLoginResponseDto;
import authapi.servicea.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        if (token == null || token.isEmpty()) {
            log.error("Token generation failed for user: {}", requestDto.getEmail());
            throw new AuthenticationException("Authentication failed "
                    + "when authenticate method was called");
        }
        return new UserLoginResponseDto(token);
    }
}
