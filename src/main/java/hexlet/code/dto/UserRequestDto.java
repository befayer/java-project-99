package hexlet.code.dto;

import hexlet.code.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto implements Serializable {
    String firstName;
    String lastName;
    @Email
    String email;
    @Size(min = 3)
    String password;
}