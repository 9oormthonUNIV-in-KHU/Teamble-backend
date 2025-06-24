package backend.teamble.user;

import backend.teamble.message.Message;
import backend.teamble.message.ReadReceipt;
import backend.teamble.project.Membership;
import backend.teamble.schedule.Schedule;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String university;
    private String college;
    private String department;
    private Boolean chatNotice;
    private Boolean scheduleNotice;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<Membership> memberships;

    @OneToMany(mappedBy = "user")
    private List<Message> messages;

    @OneToMany(mappedBy = "user")
    private List<Schedule> schedules;

    @OneToMany(mappedBy = "user")
    private List<ReadReceipt> readReceipts;
}

