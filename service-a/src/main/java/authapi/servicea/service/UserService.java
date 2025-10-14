package authapi.servicea.service;

import authapi.servicea.dto.user.UserRegistrationRequestDto;
import authapi.servicea.dto.user.UserResponseDto;
import authapi.servicea.exception.RegistrationException;
import authapi.servicea.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto registrationRequestDto)
        throws RegistrationException;

    User getAuthenticatedUser();
}
