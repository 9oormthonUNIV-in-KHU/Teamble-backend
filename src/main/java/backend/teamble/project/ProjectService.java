package backend.teamble.project;

import backend.teamble.project.dto.MyTeamsResponse;
import backend.teamble.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final MembershipRepository membershipRepository;

    @Transactional
    public Project createTeam(String teamName, String topic, User user) {
        Project project = new Project(teamName, topic);
        projectRepository.save(project);

        Membership membership = new Membership(user, project);
        membershipRepository.save(membership);

        return project;
    }

    public List<MyTeamsResponse> getMyTeams(User user) {
        List<Membership> memberships = membershipRepository.findByUser(user);

        return memberships.stream()
                .map(membership -> {
                    Project project = membership.getProject();
                    return new MyTeamsResponse(project.getId(), project.getName());
                })
                .collect(Collectors.toList());
    }
}
