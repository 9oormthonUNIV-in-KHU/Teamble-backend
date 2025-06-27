package backend.teamble.project;

import backend.teamble.message.Message;
import backend.teamble.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Project")
@Data
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

    public Project(String name, String topic) {
        this.name = name;
        this.topic = topic;
    }

    @OneToMany(mappedBy = "project")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "project")
    private List<Message> messages;

    @OneToMany(mappedBy = "project")
    private List<Membership> memberships;




}

