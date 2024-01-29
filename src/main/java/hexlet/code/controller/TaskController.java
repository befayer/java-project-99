package hexlet.code.controller;

import hexlet.code.dto.TaskParams;
import hexlet.code.dto.TaskRequest;
import hexlet.code.dto.TaskResponse;
import hexlet.code.service.TaskService;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public final class TaskController {

    private final TaskService taskService;

    /**
     * Updates a task by its ID.
     *
     * @param id          The ID of the task to be updated.
     * @param taskRequest The request containing updated task information.
     * @return ResponseEntity with the updated task response or an error response.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateById(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        TaskResponse updatedTask = taskService.updateById(id, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Creates a new task.
     *
     * @param taskRequest The request containing task information.
     * @return ResponseEntity with the created task response or an error response.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> save(@RequestBody TaskRequest taskRequest) {
        TaskResponse createdTask = taskService.save(taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Finds a task by its ID.
     *
     * @param id The ID of the task to be retrieved.
     * @return ResponseEntity with the task response or an error response.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(@PathVariable Long id) {
        TaskResponse task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Finds all tasks based on the provided query parameters.
     *
     * @param taskParams The parameters for querying tasks.
     * @return ResponseEntity with a list of task responses and headers including total count.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> findAll(TaskParams taskParams) {
        List<TaskResponse> tasks = taskService.findAll(taskParams);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(tasks.size()));
        return new ResponseEntity<>(tasks, headers, HttpStatus.OK);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to be deleted.
     * @return ResponseEntity indicating the success or failure of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
