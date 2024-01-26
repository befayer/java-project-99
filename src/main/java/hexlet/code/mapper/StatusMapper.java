package hexlet.code.mapper;

import hexlet.code.dto.TaskStatusRequest;
import hexlet.code.dto.TaskStatusResponse;
import hexlet.code.entity.TaskStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusMapper {
    TaskStatusResponse toDto(TaskStatus taskStatus);

    TaskStatus toEntity(TaskStatusRequest taskStatusRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    TaskStatus partialUpdate(TaskStatusRequest taskStatusRequest, @MappingTarget TaskStatus taskStatus);
}
