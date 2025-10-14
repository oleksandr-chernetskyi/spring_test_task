package authapi.servicea.mapper;

import authapi.servicea.dto.user.UserRegistrationRequestDto;
import authapi.servicea.dto.user.UserResponseDto;
import authapi.servicea.model.User;
import org.mapstruct.Mapper;
import authapi.servicea.config.MapperConfig;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
