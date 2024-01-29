package hexlet.code.service;

import hexlet.code.dto.TaskParams;
import hexlet.code.dto.TaskRequest;
import hexlet.code.dto.TaskResponse;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.entity.Label;
import hexlet.code.entity.Task;
import hexlet.code.entity.TaskStatus;
import hexlet.code.entity.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Service class for handling Task-related operations.
 */
@RequiredArgsConstructor
@Service
public final class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TaskSpecification taskSpecification;

    /**
     * Finds a task by its ID.
     *
     * @param id The ID of the task to be retrieved.
     * @return The task response DTO.
     */
    public TaskResponse findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        return taskMapper.toTaskResponse(task);
    }

    /**
     * Finds all tasks based on the specified parameters.
     *
     * @param taskParams The parameters to filter tasks.
     * @return List of task response DTOs.
     */
    public List<TaskResponse> findAll(TaskParams taskParams) {
        Specification<Task> specification = taskSpecification.build(taskParams);
        return taskRepository.findAll(specification).stream().map(taskMapper::toTaskResponse).toList();
    }

    /**
     * Saves a new task.
     *
     * @param taskRequest The request DTO containing task information.
     * @return The created task response DTO.
     */
    public TaskResponse save(TaskRequest taskRequest) {
        Task task = taskMapper.toTask(taskRequest);
        setAssociations(taskRequest, task);
        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }

    /**
     * Updates a task by its ID.
     *
     * @param id           The ID of the task to be updated.
     * @param taskRequest The request DTO containing updated task information.
     * @return The updated task response DTO.
     */
    public TaskResponse updateById(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id).orElseThrow();
        Task updated = taskMapper.partialUpdate(taskRequest, task);
        setAssociations(taskRequest, updated);
        Task saved = taskRepository.save(updated);
        return taskMapper.toTaskResponse(saved);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to be deleted.
     */
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    private void setAssociations(TaskRequest taskRequest, Task task) {
        TaskStatus taskStatus = null;
        if (taskRequest.getStatus() != null) {
            taskStatus = taskStatusRepository.findBySlug(taskRequest.getStatus()).orElseThrow();
        }
        User user = null;
        if (taskRequest.getAssigneeId() != null) {
            user = userRepository.findById(taskRequest.getAssigneeId()).orElseThrow();
        }
        List<Label> labels = null;
        if (taskRequest.getTaskLabelIds() != null) {
            labels = labelRepository.findAllById(taskRequest.getTaskLabelIds());
        }
        task.setTaskStatus(taskStatus);
        task.setAssignee(user);
        task.setLabels(labels != null ? new HashSet<>(labels) : new HashSet<>());
    }
}
