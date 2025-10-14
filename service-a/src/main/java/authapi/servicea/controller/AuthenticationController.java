package authapi.servicea.controller;

import authapi.servicea.dto.user.UserLoginRequestDto;
import authapi.servicea.dto.user.UserLoginResponseDto;
import authapi.servicea.dto.user.UserRegistrationRequestDto;
import authapi.servicea.dto.user.UserResponseDto;
import authapi.servicea.exception.RegistrationException;
import authapi.servicea.security.AuthenticationService;
import authapi.servicea.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody @Valid
                                    UserRegistrationRequestDto registrationRequestDto)
            throws RegistrationException {
        log.info("Received information about registration user with email: {}",
                registrationRequestDto.getEmail());
        return userService.register(registrationRequestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        log.info("Received login request for user with email: {}", userLoginRequestDto.getEmail());
        return authenticationService.authenticate(userLoginRequestDto);
    }
}
