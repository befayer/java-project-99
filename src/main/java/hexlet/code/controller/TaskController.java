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
import org.springframework.web.bind.annotation.ResponseStatus;
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
     * @return The updated task response.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateById(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        return taskService.updateById(id, taskRequest);
    }

    /**
     * Creates a new task.
     *
     * @param taskRequest The request containing task information.
     * @return The created task response.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse save(@RequestBody TaskRequest taskRequest) {
        return taskService.save(taskRequest);
    }

    /**
     * Finds a task by its ID.
     *
     * @param id The ID of the task to be retrieved.
     * @return The task response.
     */
    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    /**
     * Finds all tasks.
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
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}
