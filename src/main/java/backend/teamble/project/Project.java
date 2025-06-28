package backend.teamble.project;

import backend.teamble.message.Message;
import backend.teamble.schedule.Schedule;
import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Project")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String topic;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Project(String name, String topic, User user) {
        this.name = name;
        this.topic = topic;
        this.user = user;
    }

    @OneToMany(mappedBy = "project")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "project")
    private List<Message> messages;

    @OneToMany(mappedBy = "project")
    private List<Membership> memberships;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

