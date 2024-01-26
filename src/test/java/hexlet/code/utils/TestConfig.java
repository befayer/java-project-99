package hexlet.code.utils;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    /**
     * This method returns the Faker instance.
     * @return The Faker instance.
     */
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
