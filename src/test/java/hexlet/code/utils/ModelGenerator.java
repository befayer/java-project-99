package hexlet.code.utils;

import hexlet.code.entity.Task;
import hexlet.code.entity.User;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating model instances for testing.
 */
@Component
public class ModelGenerator {

    private Model<Task> taskModel;
    private Model<User> userModel;

    @Autowired
    private Faker faker;

    /**
     * Initialize the taskModel and userModel with Instancio configurations.
     */
    @PostConstruct
    private void init() {
        // Configure userModel
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .toModel();

        // Configure taskModel
        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.gameOfThrones().house())
                .supply(Select.field(Task::getDescription), () -> faker.gameOfThrones().quote())
                .toModel();
    }

    /**
     * Get the configured task model.
     *
     * @return The task model.
     */
    public Model<Task> getTaskModel() {
        return taskModel;
    }

    /**
     * Get the configured user model.
     *
     * @return The user model.
     */
    public Model<User> getUserModel() {
        return userModel;
    }
}
