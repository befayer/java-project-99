package hexlet.code.mapper;

import hexlet.code.dto.UserRequestDto;
import hexlet.code.dto.UserResponseDto;
import hexlet.code.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserRequestDto userRequestDto, @MappingTarget User user);
}