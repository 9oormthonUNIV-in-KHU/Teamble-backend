package backend.teamble.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeamRoleResponse {
    private Long userId;
    private String name;
    private String role;
}
