package backend.teamble.schedule;

import backend.teamble.project.Project;
import backend.teamble.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer defaultAlarmMinutesBefore;

    public Schedule(String title, LocalDateTime date, Project team, User user) {
        this.title = title;
        this.endTime = date;
        this.project = team;
        this.user = user;

        this.defaultAlarmMinutesBefore = 60;
    }
}

