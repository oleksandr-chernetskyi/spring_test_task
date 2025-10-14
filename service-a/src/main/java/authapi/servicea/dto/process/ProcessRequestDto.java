package authapi.servicea.dto.process;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProcessRequestDto {
    @NotNull
    @Size(min = 1, max = 2048)
    private String text;
}
