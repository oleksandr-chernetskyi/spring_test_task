package authapi.servicea.dto.user;

import java.util.UUID;
import lombok.Data;

@Data
public class UserResponseDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
}
