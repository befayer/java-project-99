package hexlet.code.mapper;

import hexlet.code.dto.LabelRequest;
import hexlet.code.dto.LabelResponse;
import hexlet.code.entity.Label;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LabelMapper {
    LabelResponse toLabelResponse(Label label);

    Label toEntity(LabelRequest labelRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Label partialUpdate(LabelRequest labelRequest, @MappingTarget Label label);
}
