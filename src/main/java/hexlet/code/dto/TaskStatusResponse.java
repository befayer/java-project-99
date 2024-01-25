package hexlet.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusResponse implements Serializable {
    private Long id;
    private String name;
    private String slug;
    private Instant createdAt;
}
