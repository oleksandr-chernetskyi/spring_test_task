package authapi.servicea.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotNull
    @Size(min = 6, max = 30)
    @Email
    private String email;

    @NotNull
    @Size(min = 6, max = 30)
    private String password;
}
