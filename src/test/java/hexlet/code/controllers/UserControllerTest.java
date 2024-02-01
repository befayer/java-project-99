package hexlet.code.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.UserRequestDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.ModelGenerator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link hexlet.code.controller.UserController}.
 */
@SpringBootTest(
        properties = {
            "spring.jpa.defer-datasource-initialization=false",
            "spring.sql.init.mode=never"
        }
)
@AutoConfigureMockMvc
public class UserControllerTest {

    private static final String API_USERS_PATH = "/api/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelGenerator modelGenerator;

    private User testUser;

    /**
     * Javadoc for the 'beforeEach' method.
     * This method is annotated with {@code @BeforeEach}, indicating that it should be executed before each test method.
     * It is responsible for setting up the necessary test data, in this case, creating a test user instance
     * using the configured {@link ModelGenerator#getUserModel()} and saving it to the user repository.
     *
     * This method ensures that a fresh test user is available before each test, providing a clean state for testing.
     */
    @BeforeEach
    public void beforeEach() {
        testUser = Instancio.of(modelGenerator.getUserModel()).create();
        assertDoesNotThrow(() -> userRepository.save(testUser));
    }

    /**
     * Test find all.
     */
    @Test
    @DisplayName("Test find all")
    public void findAllTest() throws Exception {
        var expectedSize = userRepository.findAll().size();
        System.out.println(expectedSize);

        mockMvc.perform(get(API_USERS_PATH)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(expectedSize)))
                .andDo(print());
    }

    /**
     * Test delete by id.
     */
    @Test
    @DisplayName("Test delete by id")
    public void deleteByIdTest() throws Exception {
        mockMvc.perform(delete(API_USERS_PATH + "/{id}", testUser.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getEmail())))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    /**
     * Test find by id.
     */
    @Test
    @DisplayName("Test find by id")
    public void findByIdTest() throws Exception {
        mockMvc.perform(get(API_USERS_PATH + "/{id}", testUser.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * Test update by id.
     */
    @Test
    @DisplayName("Test update by id")
    public void updateByIdTest() throws Exception {
        UserRequestDto data = new UserRequestDto();
        data.setFirstName("New name");

        mockMvc.perform(put(API_USERS_PATH + "/{id}", testUser.getId())
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(testUser.getEmail())))
                .andExpect(status().isOk())
                .andDo(print());

        var updatedUser = userRepository.findById(testUser.getId());
        assertTrue(updatedUser.isPresent());
        assertEquals(data.getFirstName(), updatedUser.get().getFirstName());

    }

    /**
     * Test save.
     */
    @Test
    @DisplayName("Test save")
    public void saveTest() throws Exception {
        User data = Instancio.of(modelGenerator.getUserModel())
                .create();

        mockMvc.perform(post(API_USERS_PATH)
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user("user")))
                .andExpect(status().isCreated())
                .andDo(print());

        var user = userRepository.findByEmail(data.getEmail());
        assertNotNull(user);
        assertEquals(data.getEmail(), user.get().getEmail());
    }
}
