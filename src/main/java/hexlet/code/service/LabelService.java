package hexlet.code.service;

import hexlet.code.dto.LabelRequest;
import hexlet.code.dto.LabelResponse;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.entity.Label;
import hexlet.code.repository.LabelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling Label-related operations.
 */
@RequiredArgsConstructor
@Service
public final class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    /**
     * Finds a label by its ID.
     *
     * @param id The ID of the label to be retrieved.
     * @return The label response DTO.
     */
    public LabelResponse findById(Long id) {
        Label label = labelRepository.findById(id).orElseThrow();
        return labelMapper.toLabelResponse(label);
    }

    /**
     * Finds all labels.
     *
     * @return List of label response DTOs.
     */
    public List<LabelResponse> findAll() {
        return labelRepository.findAll().stream().map(labelMapper::toLabelResponse).toList();
    }

    /**
     * Saves a new label.
     *
     * @param labelRequest The request DTO containing label information.
     * @return The created label response DTO.
     */
    public LabelResponse save(LabelRequest labelRequest) {
        Label label = labelMapper.toEntity(labelRequest);
        Label saved = labelRepository.save(label);
        return labelMapper.toLabelResponse(saved);
    }

    /**
     * Updates a label by its ID.
     *
     * @param id            The ID of the label to be updated.
     * @param labelRequest  The request DTO containing updated label information.
     * @return The updated label response DTO.
     */
    public LabelResponse updateById(Long id, LabelRequest labelRequest) {
        Label label = labelRepository.findById(id).orElseThrow();
        Label updated = labelMapper.partialUpdate(labelRequest, label);
        Label saved = labelRepository.save(updated);
        return labelMapper.toLabelResponse(saved);
    }

    /**
     * Deletes a label by its ID.
     *
     * @param id The ID of the label to be deleted.
     */
    public void deleteById(Long id) {
        labelRepository.deleteById(id);
    }
}
