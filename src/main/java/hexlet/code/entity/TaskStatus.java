package hexlet.code.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "statuses")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 1)
    @Column(name = "name", unique = true)
    private String name;

    @Size(min = 1)
    @Column(name = "slug", unique = true)
    private String slug;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;
}
