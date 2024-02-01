package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskRequest;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.ModelGenerator;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Test class for the {@link hexlet.code.controller.TaskController}.
 */
@SpringBootTest(
        properties = {
            "spring.jpa.defer-datasource-initialization=false",
            "spring.sql.init.mode=never"
        }
)
@AutoConfigureMockMvc
public class TaskControllerTest {

    private static final String API_TASK_PATH = "/api/tasks";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private ModelGenerator modelGenerator;

    private Task testTask;

    /**
     * Setup method for initializing the test environment before each test.
     * Creates a test Task with 'draft' TaskStatus, no assignee, and an empty set of labels.
     */
    @BeforeEach
    public void beforeEach() {
        TaskStatus taskStatus = taskStatusRepository.findBySlug("draft").orElseThrow();
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .set(Select.field(Task::getAssignee), null)
                .create();
        testTask.setTaskStatus(taskStatus);
        testTask.setLabels(Set.of());
        assertDoesNotThrow(() -> taskRepository.save(testTask));
    }

    /**
     * Test delete by id.
     */
    @Test
    @DisplayName("Test delete by id")
    public void deleteByIdTest() throws Exception {
        mockMvc.perform(delete(API_TASK_PATH + "/{id}", testTask.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertFalse(taskRepository.existsById(testTask.getId()));
    }

    /**
     * Test find all.
     */
    @Test
    @DisplayName("Test find all")
    public void findAllTest() throws Exception {
        var expectedSize = taskRepository.findAll().size();
        System.out.println(expectedSize);

        mockMvc.perform(get(API_TASK_PATH)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedSize)))
                .andDo(print());
    }

    /**
     * Test find by id.
     */
    @Test
    @DisplayName("Test find by id")
    public void findByIdTest() throws Exception {
        mockMvc.perform(get(API_TASK_PATH + "/{id}", testTask.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTask.getId()))
                .andExpect(jsonPath("$.title").value(testTask.getName()))
                .andExpect(jsonPath("$.index").value(testTask.getIndex()))
                .andExpect(jsonPath("$.content").value(testTask.getDescription()))
                .andExpect(jsonPath("$.assignee_id").value(testTask.getAssignee()))
                .andDo(print());
    }

    /**
     * Test save.
     */
    @Test
    @DisplayName("Test save")
    public void saveTest() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findBySlug("draft").get();
        Label label = labelRepository.findByName("feature").get();
        TaskRequest data = new TaskRequest();
        String name = "New Task Name";
        data.setTitle(name);
        data.setStatus(taskStatus.getSlug());
        data.setTaskLabelIds(Set.of(label.getId()));

        mockMvc.perform(post(API_TASK_PATH)
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")))
                .andExpect(status().isCreated())
                .andDo(print());

        var task = taskRepository.findByName(name);
        assertNotNull(task.get());
        assertEquals(name, task.get().getName());
    }

    /**
     * Test update by id.
     */
    @Test
    @DisplayName("Test update by id")
    public void updateByIdTest() throws Exception {
        Task data = Instancio.of(modelGenerator.getTaskModel())
                .create();

        mockMvc.perform(put(API_TASK_PATH + "/{id}", testTask.getId())
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")))
                .andExpect(status().isOk())
                .andDo(print());

        var updatedTask = taskRepository.findById(testTask.getId());
        assertTrue(updatedTask.isPresent());
    }
}
