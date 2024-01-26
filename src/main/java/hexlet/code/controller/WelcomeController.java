package hexlet.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling welcome requests.
 */
@RestController
public final class WelcomeController {

    /**
     * Handles the "/welcome" endpoint.
     *
     * @return A welcome message.
     */
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Spring";
    }
}
