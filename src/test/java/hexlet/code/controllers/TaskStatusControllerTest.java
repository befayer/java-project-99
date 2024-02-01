package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for the {@link hexlet.code.controller.TaskStatusController}.
 */
@SpringBootTest(
        properties = {
            "spring.jpa.defer-datasource-initialization=false",
            "spring.sql.init.mode=never"
        }
)
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    private static final String API_TASK_STATUSES_PATH = "/api/task_statuses";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private TaskStatus testTaskStatus;

    /**
     * Setup method to initialize the test environment before each test.
     * Creates a test instance of {@link TaskStatus} using Instancio,
     * ignoring the 'id' field, and saves it to the repository.
     */
    @BeforeEach
    public void beforeEach() {
        testTaskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus.class, "id"))
                .create();
        assertDoesNotThrow(() -> taskStatusRepository.save(testTaskStatus));
    }

    /**
     * Test method for deleting a TaskStatus by ID.
     * Expects a successful response with status code 204 (No Content).
     */
    @Test
    @DisplayName("Test delete")
    public void deleteTest() throws Exception {
        mockMvc.perform(delete(API_TASK_STATUSES_PATH + "/{id}", testTaskStatus.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status()
                        .isNoContent())
                .andDo(print());

        assertFalse(taskStatusRepository.existsById(testTaskStatus.getId()));
    }

    /**
     * Test method for finding all TaskStatus instances.
     * Expects a successful response with status code 200 (OK).
     */
    @Test
    @DisplayName("Test find all")
    public void findAllTest() throws Exception {
        mockMvc.perform(get(API_TASK_STATUSES_PATH)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status()
                        .isOk())
                .andDo(print());
    }

    /**
     * Test method for finding a TaskStatus by ID.
     * Expects a successful response with status code 200 (OK).
     */
    @Test
    @DisplayName("Test find by id")
    public void findByIdTest() throws Exception {
        mockMvc.perform(get(API_TASK_STATUSES_PATH + "/{id}", testTaskStatus.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$.id").value(testTaskStatus.getId()))
                .andExpect(jsonPath("$.name").value(testTaskStatus.getName()))
                .andDo(print());
    }

    /**
     * Test method for saving a new TaskStatus.
     * Expects a successful response with status code 201 (Created).
     * Also, checks if the saved TaskStatus can be retrieved from the repository.
     */
    @Test
    @DisplayName("Test save")
    public void saveTest() throws Exception {
        var data = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus.class, "id"))
                .create();

        mockMvc.perform(post(API_TASK_STATUSES_PATH)
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status()
                        .isCreated())
                .andDo(print());

        var taskStatus = taskStatusRepository.findBySlug(data.getSlug());
        assertNotNull(taskStatus.get());
        assertEquals(data.getName(), taskStatus.get().getName());
    }

    /**
     * Test method for updating a TaskStatus by ID.
     * Expects a successful response with status code 200 (OK).
     */
    @Test
    @DisplayName("Test update by id")
    public void updateByIdTest() throws Exception {
        var data = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus.class, "id"))
                .create();

        mockMvc.perform(put(API_TASK_STATUSES_PATH + "/{id}", testTaskStatus.getId())
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status()
                        .isOk())
                .andDo(print());

        var updatedTaskStatus = taskStatusRepository.findById(testTaskStatus.getId());
        assertTrue(updatedTaskStatus.isPresent());
        assertEquals(data.getName(), updatedTaskStatus.get().getName());
    }
}
