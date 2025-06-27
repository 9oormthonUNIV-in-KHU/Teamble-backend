package backend.teamble.message;

import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Read_receipt")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadReceipt {

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

}