package backend.teamble.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MyScheduleResponse {
    private Long scheduleId;
    private String title;
    private LocalDateTime date;
}
