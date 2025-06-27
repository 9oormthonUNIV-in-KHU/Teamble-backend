package backend.teamble.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTeamRequest {
    private String teamName;
    private String topic;
}
