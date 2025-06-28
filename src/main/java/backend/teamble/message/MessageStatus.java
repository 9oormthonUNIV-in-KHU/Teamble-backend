package backend.teamble.message;

import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_id", nullable = false)
    private Message message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false, length = 20)
    private String responded = "pending";  // 초기값 "pending"
}