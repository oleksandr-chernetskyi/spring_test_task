package dataapi.serviceb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransformRequestDto {
    @NotNull
    private String text;
}
