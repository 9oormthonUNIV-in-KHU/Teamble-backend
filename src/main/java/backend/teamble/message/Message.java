package backend.teamble.message;

import backend.teamble.project.Project;
import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;

    private String content;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "message")
    private List<ReadReceipt> readReceipts;
}

