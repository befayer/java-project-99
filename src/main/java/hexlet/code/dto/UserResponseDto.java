package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link hexlet.code.entity.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private Instant createdAt;
}