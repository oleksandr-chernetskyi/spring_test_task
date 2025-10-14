package authapi.servicea.dto.user;

import authapi.servicea.validation.PasswordFieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordFieldMatch(field = "password", fieldMatch = "repeatPassword")
public class UserRegistrationRequestDto {
    @Email
    @NotNull
    private String email;
    @NotNull
    @Size(min = 6, max = 30)
    private String password;
    @NotNull
    @Size(min = 6, max = 30)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}
