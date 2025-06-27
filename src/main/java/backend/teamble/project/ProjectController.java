package backend.teamble.project;

import backend.teamble.project.dto.CreateTeamRequest;
import backend.teamble.project.dto.CreateTeamResponse;
import backend.teamble.project.dto.MyTeamsResponse;
import backend.teamble.project.dto.MyTeamsResponseWrapper;
import backend.teamble.security.CustomUserDetails;
import backend.teamble.user.User;
import backend.teamble.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
