package backend.teamble.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateScheduleResponse {
    private Long ScheduleId;
    private String message;
}
