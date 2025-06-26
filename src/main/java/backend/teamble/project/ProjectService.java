package backend.teamble.project;

import backend.teamble.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
