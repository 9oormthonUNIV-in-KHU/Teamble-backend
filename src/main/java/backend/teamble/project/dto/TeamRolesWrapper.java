package backend.teamble.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamRolesWrapper {
    private List<TeamRoleResponse> roles;
}

