package backend.teamble.project;

import backend.teamble.message.Message;
import backend.teamble.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private Long id;

    private String name;
    private String topic;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "project")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "project")
    private List<Message> messages;

    @OneToMany(mappedBy = "project")
    private List<Membership> memberships;
}

