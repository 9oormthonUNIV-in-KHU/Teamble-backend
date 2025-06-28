package backend.teamble.message;

import backend.teamble.project.Project;
import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Message")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id") // _id 형식에 맞춰 수정
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private String type="text";

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Boolean responded = false;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageStatus> messageStatuses;


}