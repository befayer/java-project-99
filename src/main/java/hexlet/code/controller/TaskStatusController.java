package hexlet.code.controller;

import hexlet.code.dto.TaskStatusRequest;
import hexlet.code.dto.TaskStatusResponse;
import hexlet.code.service.StatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@RequiredArgsConstructor
public final class TaskStatusController {

    private final StatusService statusService;

    /**
     * Finds a task status by its ID.
     *
     * @param id The ID of the task status to be retrieved.
     * @return The task status response.
     */
    @GetMapping("/{id}")
    public TaskStatusResponse findById(@PathVariable Long id) {
        return statusService.findById(id);
    }

    /**
     * Finds all task statuses.
     *
     * @return ResponseEntity with a list of task status responses and headers including total count.
     */
    @GetMapping
    public ResponseEntity<List<TaskStatusResponse>> findAll() {
        List<TaskStatusResponse> statuses = statusService.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(statuses.size()));
        return new ResponseEntity<>(statuses, headers, HttpStatus.OK);
    }

    /**
     * Updates a task status by its ID.
     *
     * @param id                 The ID of the task status to be updated.
     * @param taskStatusRequest The request containing updated task status information.
     * @return The updated task status response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusResponse> updateById(@PathVariable Long id,
                                         @RequestBody @Valid TaskStatusRequest taskStatusRequest) {
        return ResponseEntity.ok(statusService.updateById(id, taskStatusRequest));
    }

    /**
     * Deletes a task status by its ID.
     *
     * @param id The ID of the task status to be deleted.
     * @return ResponseEntity indicating the success of the deletion (HTTP 204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        statusService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Creates a new task status.
     *
     * @param taskStatusRequest The request containing task status information.
     * @return The created task status response.
     */
    @PostMapping
    public ResponseEntity<TaskStatusResponse> save(@RequestBody @Valid TaskStatusRequest taskStatusRequest) {
        return new ResponseEntity<>(statusService.save(taskStatusRequest), HttpStatus.CREATED);
    }
}
