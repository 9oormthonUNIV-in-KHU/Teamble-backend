package backend.teamble.user;

import backend.teamble.message.Message;
import backend.teamble.project.Membership;
import backend.teamble.project.Project;
import backend.teamble.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String university;

    private String college;

    private String department;

    private Boolean chatNotice = true;

    private Boolean scheduleNotice = true;

    @Column(nullable = false)
    private String role = "USER"; // 기본값을 "USER"로 설정

    @OneToMany(mappedBy = "user")
    private List<Membership> memberships;

    @OneToMany(mappedBy = "user")
    private List<Message> messages;

    @OneToMany(mappedBy = "user")
    private List<Schedule> schedules;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true)
    private Project project;
}