package backend.teamble.schedule.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateScheduleRequest {
    private Long teamId;
    private String title;
    private LocalDateTime date;
}
