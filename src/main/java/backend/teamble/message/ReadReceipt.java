package backend.teamble.message;

import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ReadReceipt",
        uniqueConstraints = @UniqueConstraint(columnNames = {"messageId", "userId"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReadReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "messageId", nullable = false)
    private Message message;

    private LocalDateTime readAt;
}


