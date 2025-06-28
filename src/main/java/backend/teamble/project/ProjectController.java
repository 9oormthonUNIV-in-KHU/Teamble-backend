package backend.teamble.project;

import backend.teamble.project.dto.*;
import backend.teamble.security.CustomUserDetails;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final UserRepository userRepository;

    @PostMapping("/teams")
    ResponseEntity<?> addTeam(@RequestBody CreateTeamRequest createTeamRequest, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Project project = projectService.createTeam(createTeamRequest.getTeamName(), createTeamRequest.getTopic(), user);
        CreateTeamResponse response = new CreateTeamResponse(project.getId(), "팀이 성공적으로 생성되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/teams/my")
    ResponseEntity<?> getMyTeams(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<MyTeamsResponse> response = projectService.getMyTeams(user);
        return ResponseEntity.ok(new MyTeamsResponseWrapper(response));
    }

    @PostMapping("/teams/{teamId}/invite")
    ResponseEntity<?> inviteUserToTeam(@PathVariable Long teamId, @RequestBody InviteRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User inviter = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        projectService.inviteUser(teamId, request.getEmail(), inviter);

        return ResponseEntity.ok(new InviteResponse("팀원 초대가 전송되었습니다."));
    }

    @GetMapping("/teams/{teamId}/roles")
    public ResponseEntity<TeamRolesWrapper> getTeamRoles(@PathVariable Long teamId) {
        TeamRolesWrapper rolesWrapper = new TeamRolesWrapper(projectService.getTeamRoles(teamId));
        return ResponseEntity.ok(rolesWrapper);
    }

}
