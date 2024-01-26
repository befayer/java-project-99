package hexlet.code.service;

import hexlet.code.dto.TaskStatusRequest;
import hexlet.code.dto.TaskStatusResponse;
import hexlet.code.mapper.StatusMapper;
import hexlet.code.entity.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for handling Task Status-related operations.
 */
@RequiredArgsConstructor
@Service
public final class StatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final StatusMapper statusMapper;

    /**
     * Finds a task status by its ID.
     *
     * @param id The ID of the task status to be retrieved.
     * @return The task status response DTO.
     */
    public TaskStatusResponse findById(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow();
        return statusMapper.toDto(taskStatus);
    }

    /**
     * Finds all task statuses.
     *
     * @return List of task status response DTOs.
     */
    public List<TaskStatusResponse> findAll() {
        return taskStatusRepository.findAll().stream().map(statusMapper::toDto).toList();
    }

    /**
     * Saves a new task status.
     *
     * @param taskStatusRequest The request DTO containing task status information.
     * @return The created task status response DTO.
     */
    public TaskStatusResponse save(TaskStatusRequest taskStatusRequest) {
        TaskStatus taskStatus = statusMapper.toEntity(taskStatusRequest);
        TaskStatus saved = taskStatusRepository.save(taskStatus);
        return statusMapper.toDto(saved);
    }

    /**
     * Updates a task status by its ID.
     *
     * @param id                  The ID of the task status to be updated.
     * @param taskStatusRequest  The request DTO containing updated task status information.
     * @return The updated task status response DTO.
     */
    public TaskStatusResponse updateById(Long id, TaskStatusRequest taskStatusRequest) {
        TaskStatus taskStatus = taskStatusRepository.findById(id).orElseThrow();
        TaskStatus updated = statusMapper.partialUpdate(taskStatusRequest, taskStatus);
        TaskStatus saved = taskStatusRepository.save(updated);
        return statusMapper.toDto(saved);
    }

    /**
     * Deletes a task status by its ID.
     *
     * @param id The ID of the task status to be deleted.
     */
    public void deleteById(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
