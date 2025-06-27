package backend.teamble.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyTeamsResponse {
    private Long teamId;
    private String teamName;
}
